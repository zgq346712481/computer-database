package com.excilys.cdb.config;

import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.controller.Controller;
import com.excilys.cdb.mapper.resultset.ResultSetToCompanyMapper;
import com.excilys.cdb.mapper.resultset.ResultSetToComputerMapper;
import com.excilys.cdb.mapper.resultset.ResultSetToListMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.servlet.pagination.Pagination;
import com.excilys.cdb.servlet.pagination.PaginationParameters;
import com.excilys.cdb.servlet.sorting.Sorting;
import com.excilys.cdb.servlet.sorting.SortingParameters;
import com.excilys.cdb.ui.Cli;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages = "com.excilys.cdb")
public class AppConfig {

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource(HikariConfig configuration) {
	TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	return new HikariDataSource(configuration);
    }

    @Bean
    public HikariConfig hikariConfig() {
	return new HikariConfig("/datasource.properties");
    }

    @Bean
    public ResultSetToListMapper<Company> companyResultSetToListMapper(
	    ResultSetToCompanyMapper resultSetToCompanyMapper) {
	return new ResultSetToListMapper<>(resultSetToCompanyMapper);
    }

    @Bean
    public ResultSetToListMapper<Computer> computerResultSetToListMapper(
	    ResultSetToComputerMapper resultSetToComputerMapper) {
	return new ResultSetToListMapper<>(resultSetToComputerMapper);
    }

    @Bean
    public Cli cli(Controller controller) {
	return new Cli(controller, System.in, System.out);
    }

    @Bean
    public PaginationParameters paginationParameters() {
	final int MIN_PAGE_SIZE = 10;
	final int MAX_PAGE_SIZE = 100;
	final int DEFAULT_PAGE_SIZE = 50;
	final String PARAMETER_NEXT = "next";
	final String PARAMETER_PAGE = "page";
	final String PARAMETER_PAGES = "pages";
	final String PARAMETER_PREVIOUS = "previous";
	final String PARAMETER_SIZE = "size";
	final String PARAMETER_ORDER_BY = "order-by";
	final String PARAMETER_MEANING = "meaning";

	return new PaginationParameters(MIN_PAGE_SIZE, MAX_PAGE_SIZE, DEFAULT_PAGE_SIZE, PARAMETER_SIZE, PARAMETER_PAGE,
		PARAMETER_PREVIOUS, PARAMETER_NEXT, PARAMETER_PAGES, PARAMETER_ORDER_BY, PARAMETER_MEANING);
    }

    @Bean
    public Pagination pagination(PaginationParameters parameters) {
	return new Pagination(parameters);
    }

    @Bean
    public SortingParameters sortingParameters() {
	final String PARAMETER_ORDER_BY = "order-by";
	final String PARAMETER_MEANING = "meaning";
	final String PARAMETER_ORDER_BY_UTILS = "order-utils";

	return new SortingParameters(PARAMETER_ORDER_BY, PARAMETER_MEANING, PARAMETER_ORDER_BY_UTILS);
    }

    @Bean
    public Sorting sorting(SortingParameters parameters) {
	return new Sorting(parameters);
    }

}