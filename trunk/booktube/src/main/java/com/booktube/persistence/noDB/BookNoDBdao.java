package com.booktube.persistence.noDB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import com.booktube.model.Book;
import com.booktube.model.Comment;
import com.booktube.model.User;
import com.booktube.persistence.BookDao;
import com.booktube.service.BookService.SearchType;
import com.booktube.service.UserServiceImpl;

public class BookNoDBdao implements BookDao {
    //private static Map usersMap;
    private static ArrayList<Book> books;
    //private static UserNoDBdao userDao = new UserNoDBdao();
    //private static List<User> authors = userDao.getAllUsers();
    private static UserServiceImpl userServiceImpl = new UserServiceImpl();
    private static List<User> authors = userServiceImpl.getAllUsers(0, Integer.MAX_VALUE);
    
    static {
        books = new ArrayList<Book>();
        books.add(new Book(new Long(1), "bookTitle1", "sasasasasaasa", authors.get(0)));
        books.add(new Book(new Long(2), "bookTitle2", "lalalalalal", authors.get(1)));
        
        Set<Comment> comments = new HashSet<Comment>();
        comments.add(new Comment(authors.get(0), books.get(0), "este es el comentario 1"));
        comments.add(new Comment(authors.get(0), books.get(0), "este es el comentario 2"));
        comments.add(new Comment(authors.get(0), books.get(0), "este es el comentario 3"));
        comments.add(new Comment(authors.get(0), books.get(0), "este es el comentario 4"));
        
        books.get(0).setComments(comments);
        
        Set<Comment> comments2 = new HashSet<Comment>();
        comments2.add(new Comment(authors.get(1), books.get(1), "este es el comentario 1"));
        comments2.add(new Comment(authors.get(1), books.get(1), "este es el comentario 2"));
        comments2.add(new Comment(authors.get(1), books.get(1), "este es el comentario 3"));
        comments2.add(new Comment(authors.get(1), books.get(1), "este es el comentario 4"));
        
        books.get(1).setComments(comments2);
        
    }

   // Log logger = LogFactory.getLog(this.getClass());

    public List<Book> getAllBooks() {
        return books;
    }

    public Book getBook(Integer id) {
        Book book = null;
        Iterator<Book> iter = books.iterator();
        while (iter.hasNext()) {
            book = (Book)iter.next();
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }

    public void update(Book book) {
      /*  Long id = book.getId();
        for (int i = 0; i < books.size(); i++) {
            Book tempBook = (Book)books.get(i);
            if (tempBook.getId().equals(id)) {
                emp.setDepartment((Department)departmentsMap.get(emp.getDepartment().getDepartmentId()));
                book.setAuthor((User)usersMap.get(book.getAuthor().getId()));
                employees.set(i, emp);
                break;
            }
        }*/
    }

    public void insert(Book book) {
    	Long lastId = (long) 0;
        Iterator<Book> iter = books.iterator();
        while (iter.hasNext()) {
            Book temp = (Book)iter.next();
            if (temp.getId() > lastId) {
                lastId = temp.getId();
            }
        }
        User user = userServiceImpl.getUser(book.getAuthor().getId());
        book.setAuthor(user);
        //book.setAuthor((User)authors.get(book.getAuthor().getId().intValue()));
        book.setId(new Long(lastId + 1));
        books.add(book);
    }

    public void delete(Book book) {
    	Long id = book.getId();
        for (int i = 0; i < books.size(); i++) {
            Book tempBook = (Book)books.get(i);
            if (tempBook.getId().equals(id)) {
                books.remove(i);
                break;
            }
        }
    }
    
    public void edit(Integer id, Book newBook) {
    	Book book = getBook(id);
    	book.setId(newBook.getId());
    	book.setTitle(newBook.getTitle());
    	book.setText(newBook.getText());
    	book.setAuthor(newBook.getAuthor());
    }

	public void insertComment(Comment comment) {
		// TODO Auto-generated method stub
		
	}

	public List<Book> findBookByTitle(String title, int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Iterator<Book> iterator(int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Book> findBookByTag(String tag, int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Book> findBookByAuthor(String author, int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Book> findBookByAuthor(String author) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCount(String type, String parameter) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Book> getAllBooks(int first, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCount(SearchType type, String parameter) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Book getBook(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}
