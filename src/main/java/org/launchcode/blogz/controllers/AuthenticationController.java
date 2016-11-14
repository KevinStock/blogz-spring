package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AuthenticationController extends AbstractController {
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		// parameters
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		User user;
		
		// verify parameters
		if (User.isValidUsername(username)) {
			if (userDao.findByUsername(username) != null) {
				model.addAttribute("username", username);
				model.addAttribute("username_error", "User already exists");
				return "signup";
			}
		}
		else {
			model.addAttribute("username", username);
			model.addAttribute("username_error", "Username is invalid");
			return "signup";
		}
		
		if (User.isValidPassword(password)) {
			if (password.equals(verify)) {
				user = new User(username, password);
				userDao.save(user);
				// set session data
				setUserInSession(request.getSession(), user);
				return "redirect:blog/newpost";
			}
			model.addAttribute("verify_error", "Passwords do not match.");
		}
		else {
			model.addAttribute("password_error", "Password is invalid");
		}
		model.addAttribute("username", username);
		
		return "signup";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		// parameters
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		// get user by username
		User user = userDao.findByUsername(username);
		
		if (user == null) {
			model.addAttribute("username", username);
			model.addAttribute("error", "No user with that username found.");
			return "login";
		}
		
		// validate password
		if (user.isMatchingPassword(password)) {
			// set user in session
			setUserInSession(request.getSession(), user);
			return "redirect:blog/newpost";
		}
		model.addAttribute("username", username);
		model.addAttribute("error", "Incorrect Password");
		return "login";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
