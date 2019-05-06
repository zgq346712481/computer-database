package com.excilys.cdb.persistence.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.exception.ComputerDAOException;
import com.excilys.cdb.mapper.resultset.ResultSetMapper;
import com.excilys.cdb.mapper.resultset.ResultSetToComputerMapper;
import com.excilys.cdb.mapper.resultset.ResultSetToCountMapper;
import com.excilys.cdb.mapper.resultset.ResultSetToListMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ConnectionManager;
import com.excilys.cdb.persistence.ConnectionProvider;

public class ComputerDAO {

    private static final String SQL_COUNT = "SELECT COUNT(id) AS count FROM computer";
    private static final String SQL_CREATE = "INSERT INTO computer (name, introduced,discontinued,company_id) VALUES (?,?,?,?)";
    private static final String SQL_DELETE = "DELETE FROM computer WHERE id=?";
    private static final String SQL_FIND_ALL_PAGED = "SELECT A.id AS id,A.name AS name ,A.introduced AS introduced ,A.discontinued AS discontinued ,B.id AS company_id,B.name AS company_name FROM computer AS A LEFT JOIN company AS B ON A.company_id = B.id ORDER BY A.id LIMIT ? OFFSET ?";
    private static final String SQL_FIND_BY_ID = "SELECT A.id AS id,A.name AS name ,A.introduced AS introduced ,A.discontinued AS discontinued ,B.id AS company_id,B.name AS company_name FROM computer AS A LEFT JOIN company AS B ON A.company_id = B.id WHERE A.id = ? LIMIT 1";
    private static final String SQL_UPDATE = "UPDATE computer SET name = ?, introduced = ?,discontinued = ?,company_id = ? WHERE id = ?";
    private static ComputerDAO instance;
    private final ConnectionProvider connectionManager = ConnectionManager.getInstance();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ResultSetMapper<List<Computer>> resultSetMapper = new ResultSetToListMapper<>(
	    ResultSetToComputerMapper.getInstance());
    private final ResultSetToCountMapper resultSetToCountMapper = ResultSetToCountMapper.getInstance();

    private ComputerDAO() {
    }

    public static synchronized ComputerDAO getInstance() {
	if (Objects.isNull(instance)) {
	    instance = new ComputerDAO();
	}
	return instance;
    }

    public long count() {
	try {
	    return JDBCUtils.find(resultSetToCountMapper, connectionManager, SQL_COUNT);
	} catch (SQLException e) {
	    logger.warn("count()", e);
	    throw new ComputerDAOException(e);
	}
    }

    public long create(Computer computer) {
	final SQLComputer sqlComputer = SQLComputer.from(computer);
	try {
	    return JDBCUtils.insert(connectionManager, SQL_CREATE, sqlComputer.getName(), sqlComputer.getIntroduced(),
		    sqlComputer.getDiscontinued(), sqlComputer.getManufacturerId());
	} catch (SQLException e) {
	    logger.warn("create(" + computer + ")", e);
	    throw new ComputerDAOException(e);
	}

    }

    public void deleteById(long id) {
	try {
	    JDBCUtils.delete(connectionManager, SQL_DELETE, id);
	} catch (SQLException e) {
	    logger.warn("deleteById(" + id + ")", e);
	    throw new ComputerDAOException(e);
	}
    }

    public List<Computer> findAll(long offset, long limit) {
	try {
	    return JDBCUtils.find(resultSetMapper, connectionManager, SQL_FIND_ALL_PAGED, limit, offset);
	} catch (SQLException e) {
	    logger.warn("findAll(" + offset + "," + limit + ")", e);
	    throw new ComputerDAOException(e);
	}
    }

    public Optional<Computer> findById(long id) {
	try {
	    List<Computer> computers = JDBCUtils.find(resultSetMapper, connectionManager, SQL_FIND_BY_ID, id);
	    return DAOUtils.haveOneOrEmpty(computers);
	} catch (SQLException e) {
	    logger.warn("findById(" + id + ")", e);
	    throw new ComputerDAOException(e);
	}
    }

    public void update(Computer computer) {
	final SQLComputer sqlComputer = SQLComputer.from(computer);
	try {
	    JDBCUtils.update(connectionManager, SQL_UPDATE, sqlComputer.getName(), sqlComputer.getIntroduced(),
		    sqlComputer.getDiscontinued(), sqlComputer.getManufacturerId(), sqlComputer.getId());
	} catch (SQLException e) {
	    logger.warn("update(" + computer + ")", e);
	    throw new ComputerDAOException(e);
	}

    }
}
