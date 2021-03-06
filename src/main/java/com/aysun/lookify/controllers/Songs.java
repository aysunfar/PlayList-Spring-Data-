package com.aysun.lookify.controllers;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aysun.lookify.models.Song;
import com.aysun.lookify.services.SongService;

@Controller()
public class Songs {

	private final SongService songService;
	
	public Songs(SongService songService) {
		this.songService = songService;
	}
	
	@RequestMapping("/")
	public String index() {
		return "index.jsp";
	}
	
	@GetMapping("/dashboard")
	public String dashboard(@ModelAttribute("song") Song song,BindingResult result,Model model) {
		model.addAttribute("songs", songService.getAllSongs());
		return "info.jsp";
	}

	@GetMapping("/songs/{id}")
	public String showSong(@PathVariable("id") Long id, Model model) {
		model.addAttribute("song", songService.findSongById(id));
		return "play.jsp";
	}
	
	@GetMapping("/songs/new")
	public String newSong(@ModelAttribute("song") Song song, Model model) {
		int[] ratings = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		model.addAttribute("ratings", ratings);
		return "new.jsp";
	}
	
	@PostMapping("/songs/new")
	public String createSong(@Valid @ModelAttribute("song") Song song,BindingResult result, Model model) {
		if (result.hasErrors()) {
			int[] ratings = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
			model.addAttribute("ratings", ratings);
			return "new.jsp";
		} else {
			songService.addSong(song);
			return "redirect:/dashboard";
		}
	}
	
	@GetMapping("/songs/delete/{id}")
	public String deleteSong(@PathVariable("id") Long id) {
		songService.deleteSong(id);
		return "redirect:/dashboard";
	}
	
	@GetMapping("/search/{artist}")
	public String displaySearchResults(
			@PathVariable("artist") String artist,
			@ModelAttribute("song") Song song,
			BindingResult result,
			Model model) {
		model.addAttribute("artist", artist);
		model.addAttribute("songs", songService.findSongByArtist(artist));
		return "results.jsp";
	}
	
	@PostMapping("/search")
	public String searchArtist(
			@RequestParam(value="artist") String artist,
			@Valid @ModelAttribute("song") Song song,
			BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("songs", songService.getAllSongs());
			return "info.jsp";
		} else {
			return "redirect:/search/" + artist;
		}
	}
	
	@GetMapping("/songs/top")
	public String showTopSongs(Model model) {
		model.addAttribute("songs", songService.findTopSongs());
		return "toplist.jsp";
	}
}	