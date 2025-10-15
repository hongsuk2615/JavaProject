package com.springboot.springproject.crud.service;
import com.springboot.springproject.crud.model.Board;
import com.springboot.springproject.crud.repository.BoardRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
