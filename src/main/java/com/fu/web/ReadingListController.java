package com.fu.web;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fu.po.Book;
import com.fu.po.Reader;
import com.fu.properties.AmazonProperties;
import com.fu.repository.ReadingListRepository;

@Controller
@RequestMapping("/readingList")
@ConfigurationProperties("amazon")
public class ReadingListController {

	private AmazonProperties amazonConfig;
	private ReadingListRepository readingListRepository;

	public ReadingListController(AmazonProperties amazonConfig, ReadingListRepository readingListRepository) {
		super();
		this.amazonConfig = amazonConfig;
		this.readingListRepository = readingListRepository;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fail")
	public void fail() {
		throw new RuntimeException();
	}

	@ExceptionHandler(value = RuntimeException.class)
	@ResponseStatus(value = HttpStatus.BANDWIDTH_LIMIT_EXCEEDED)
	public String error() {
		return "error";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String readersBooks(Reader reader, Model model) {
		List<Book> readingList = readingListRepository.findByReader(reader);
		if (readingList != null) {
			model.addAttribute("books", readingList);
			model.addAttribute("reader", reader);
			model.addAttribute("amazonID", amazonConfig.getAssociateId());
		}
		return "readingList";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String addToReadingList(Reader reader, Book book) {
		book.setReader(reader);
		readingListRepository.save(book);
		return "redirect:/readingList";
	}

	/*
	 * @RequestMapping(value = "/{reader}", method = RequestMethod.GET) public
	 * String readersBook(@PathVariable("reader") String reader, Model model) {
	 * List<Book> readingList = readingListRepository.findByReader(reader); if
	 * (readingList != null) { model.addAttribute("books", readingList); }
	 * return "readingList"; }
	 */

	/*
	 * @RequestMapping(value = "/{reader}", method = RequestMethod.POST) public
	 * String addToReadingList(@PathVariable("reader") String reader, Book book)
	 * { book.setReader(reader); readingListRepository.save(book); return
	 * "redirect:/{reader}"; }
	 */

}
