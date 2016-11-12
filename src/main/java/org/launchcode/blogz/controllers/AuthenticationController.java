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
	
	@Autowired
	private UserDao userDao;
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupForm() {
		return "signup";
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(HttpServletRequest request, Model model) {
		
		// TODO - implement signup
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		User user = new User(username, password);
		
		if (user.isValidUsername(user.getUsername())) {
			if (user.isValidPassword(password)) {
				if (password.equals(verify)) {
					userDao.save(user);
					request.getSession().setAttribute(AbstractController.userSessionKey, user.getUid());
					return "redirect:blog/newpost";
				}
				model.addAttribute("verify_error", "Passwords do not match.");
			}
			model.addAttribute("password_error", "Password is invalid");
		}
		model.addAttribute("username_error", "Username is invalid");
		return "signup";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		User user = userDao.findByUsername(username);
		if (user.isMatchingPassword(password)) {
			request.getSession().setAttribute(AbstractController.userSessionKey, user.getUid());
			return "redirect:blog/newpost";
		}
		model.addAttribute("error", "Error logging in");
		return "login";
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
