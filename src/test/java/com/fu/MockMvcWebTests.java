package com.fu;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fu.po.Book;
import com.fu.po.Reader;
//@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MockMvcWebTests {

  @Autowired
  WebApplicationContext webContext;
  private MockMvc mockMvc;

  @Before
  public void setupMockMvc() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(webContext)
        .build();
  }
  
  @Test
  public void redirectFromRoot() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/readingList"));
  }

  @Test
  public void homePage() throws Exception {
    mockMvc.perform(get("/readingList"))
        .andExpect(status().isOk())
        .andExpect(view().name("readingList"))
        .andExpect(model().attributeExists("books"));
  }
  
  @Test
  public void postBook() throws Exception {
    mockMvc.perform(post("/readingList")
           .contentType(APPLICATION_FORM_URLENCODED)
           .param("title", "BOOK TITLE")
           .param("author", "BOOK AUTHOR")
           .param("isbn", "1234567890")
           .param("description", "DESCRIPTION"))
           .andExpect(status().is3xxRedirection())
           .andExpect(header().string("Location", "/readingList"));
    
    Book expectedBook = new Book();
    Reader reader = new Reader();
    reader.setUsername("craig");
    reader.setFullname("Craig Walls");
    reader.setPassword("password");
    expectedBook.setId(13L);
    expectedBook.setReader(reader);
//    expectedBook.setReader("craig");
    expectedBook.setTitle("BOOK TITLE");
    expectedBook.setAuthor("BOOK AUTHOR");
    expectedBook.setIsbn("1234567890");
    expectedBook.setDescription("DESCRIPTION");
    
    mockMvc.perform(get("/readingList"))
           .andExpect(status().isOk())
           .andExpect(view().name("readingList"))
           .andExpect(model().attributeExists("books"))
           .andExpect(model().attribute("books", hasSize(1)))
           .andExpect(model().attribute("books", 
                        contains(samePropertyValuesAs(expectedBook))));
  }

}
