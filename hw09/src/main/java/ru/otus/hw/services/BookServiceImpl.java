package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(long id) {
        return bookRepository.findById(id).map(bookMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }

    @Transactional
    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }
}
