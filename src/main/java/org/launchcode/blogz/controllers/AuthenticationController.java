package org.launchcode.blogz.controllers;

import javax.servlet.http.HttpServletRequest;

import org.launchcode.blogz.models.User;
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
		// get parameters from request
		String name = request.getParameter("username");
		String password = request.getParameter("password");
		String verify = request.getParameter("verify");
		
		// validate parameteres(username, password, verify)
		//if they validate, create a new user, and put them in the session
		
		if(!User.isValidUsername(name)){
			String username_error = "That name is not valid";
			model.addAttribute("username_error", username_error);
			return "signup";
		}
		else{
			if(!User.isValidPassword(password)){
				String password_error = "Invalid password";
				model.addAttribute("password_error", password_error);
				return "signup";
			} else{
				if(!password.equals(verify)){
					String verify_error = "Passwords don't match";
					model.addAttribute("verify_error", verify_error);
					return "signup";
				}else{
					User user = new User(name, password);
					userDao.save(user);
					model.addAttribute("username", name);
					model.addAttribute("pwhash", password);
					loginHelper(request, user);
					return "redirect:blog/newpost";
				}
			}
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginForm() {
		return "login";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, Model model) {
		
		// TODO - implement login
		// get parameters from request
		String name = request.getParameter("username");
		String password = request.getParameter("password");
		
		// get user by their username
		User user = userDao.findByUsername(name);
		
		// check password is correct
		// log them in, if so(i.e. setting the user in the session)
		if(!user.isMatchingPassword(password)){
			String error = "Invalid login";
			model.addAttribute("error", error);
			return "login";
		}
		else{
			loginHelper(request, user);
			return "redirect:blog/newpost";
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
        request.getSession().invalidate();
		return "redirect:/";
	}
}
