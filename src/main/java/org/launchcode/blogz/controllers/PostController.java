package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
		
		// get parameters
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		
		String error = "";
		// validate parameters
		if (title != "" && body != "") {
			Post post = new Post(title, body, getUserFromSession(request.getSession()));
			postDao.save(post);
			return "redirect:/blog/" + post.getAuthor().getUsername() + "/" + post.getUid();  // TODO - this redirect should go to the new post's page
		}
		else {
			error = "Post title and body are required.";
		}
		model.addAttribute("value", title);
		model.addAttribute("body", body);
		model.addAttribute("error", error);
		
		return "newpost";  		
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		
		// TODO - implement singlePost
		
		// get given post
		Post post = postDao.findByUid(uid);
		
		// pass post into template
		model.addAttribute("post", post);
		//model.addAttribute("title", post.getTitle());
		//model.addAttribute("body", post.getBody());
		
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		
		// TODO - implement userPosts
		
		// get posts by user
		User user = userDao.findByUsername(username);
		List<Post> posts = user.getPosts();
		
		// pass posts into template
		model.addAttribute("posts", posts);
		
		return "blog";
	}
	
}
