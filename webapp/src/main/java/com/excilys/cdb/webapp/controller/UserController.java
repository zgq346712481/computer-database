package com.excilys.cdb.webapp.controller;

import com.excilys.cdb.model.Role;
import com.excilys.cdb.model.User;
import com.excilys.cdb.service.service.UserService;
import com.excilys.cdb.shared.dto.CreateUserDTO;
import com.excilys.cdb.shared.mapper.Mapper;
import com.excilys.cdb.shared.validator.CreateUserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final String ADD_USER_JSP = "addUser";
    private static final String PARAMETER_USER = "user";
    private static final String PARAMETER_SUCCESS = "success";
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Mapper<CreateUserDTO, User> createUserDtoToUserMapper;
    private final CreateUserValidator createUserValidator;

    public UserController(UserService userService, PasswordEncoder passwordEncoder,
                          Mapper<CreateUserDTO, User> createUserDtoToUserMapper, CreateUserValidator createUserValidator) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.createUserDtoToUserMapper = createUserDtoToUserMapper;
        this.createUserValidator = createUserValidator;
    }

    @InitBinder
    protected void initBinding(WebDataBinder dataBinder) {
        dataBinder.addValidators(createUserValidator);
    }

    @GetMapping
    public String form() {
        return ADD_USER_JSP;
    }

    @ModelAttribute(PARAMETER_USER)
    public CreateUserDTO user() {
        return new CreateUserDTO();
    }

    @PostMapping
    public ModelAndView add(@Validated @ModelAttribute(PARAMETER_USER) CreateUserDTO userDTO, BindingResult result) {
        final ModelAndView modelAndView = new ModelAndView(ADD_USER_JSP);
        if (!result.hasErrors()) {
            final User user = createUserDtoToUserMapper.map(userDTO);
            user.addRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.create(user);
            modelAndView.addObject(PARAMETER_USER, new CreateUserDTO());
        }
        modelAndView.addObject(PARAMETER_SUCCESS, !result.hasErrors());
        return modelAndView;
    }
}
