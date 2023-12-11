package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static CommentDtoOutput mapToCommentOutput(Comment comment) {
        System.out.println(comment);
        CommentDtoOutput commentDtoOutput = new CommentDtoOutput();
        commentDtoOutput.setId(comment.getId());
        commentDtoOutput.setText(comment.getText());
        commentDtoOutput.setCreated(comment.getCreated());
        commentDtoOutput.setAuthorName(comment.getAuthor().getName());
        return commentDtoOutput;
    }

    public static List<CommentDtoOutput> mapToListDto(List<Comment> comments) {
        List<CommentDtoOutput> commentList = new ArrayList<>();
        if (!comments.isEmpty()) {
            for (Comment c : comments) {
                commentList.add(mapToCommentOutput(c));
            }
        }
        return commentList;
    }

    public static Comment mapToNewComment(CommentDtoInput commentDtoInput, User author, Item item) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setText(commentDtoInput.getText());
        return comment;
    }
}
