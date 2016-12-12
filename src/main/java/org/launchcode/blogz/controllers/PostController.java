package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		// TODO - implement newPost
		//get request parameters
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		HttpSession session = request.getSession(true);
		int uid = (int)session.getAttribute(userSessionKey);
		User user = userDao.findByUid(uid);
		
		//validate parameters
		//if valid, create new Post
		//if not valid, send back to the form with error message
		
		if(!title.equals(null) && !body.equals(null))
		{		
			Post post = new Post(title, body, user);
			postDao.save(post);
			return "redirect:blog";
		}
		else
		{
			String error = "Invalid post";
			model.addAttribute("error", error);
			return "newpost";
		}
		 		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		// get the given post
		Post post = postDao.findByUid(uid); 
		
		// pass the post into the template
		model.addAttribute("post", post);
		
		return "post";
		
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		// get all of the user's posts
		User user = userDao.findByUsername(username);

		List<Post> listOfPosts = user.getPosts();
		
		//pass the posts into the template
		model.addAttribute("posts", listOfPosts);
		
		return "blog";
		
	}
	
}
