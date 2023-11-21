package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.CommentDtoOutput;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;

public class CommentMapper {

    public static CommentDtoOutput mapToCommentOutput(Comment comment) {
        CommentDtoOutput commentDtoOutput = new CommentDtoOutput();
        commentDtoOutput.setId(comment.getId());
        commentDtoOutput.setText(comment.getText());
        commentDtoOutput.setCreated(comment.getCreated());
        commentDtoOutput.setAuthorName(comment.getAuthor().getName());
        return commentDtoOutput;
    }

    public static Comment mapToNewComment(CommentDtoInput commentDtoInput, User author) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setText(commentDtoInput.getText());
        return comment;
    }
}
