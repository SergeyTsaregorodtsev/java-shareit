package ru.practicum.shareit.item;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentDto commentDto, Item item, User author) {
        return new Comment(
                commentDto.getText(),
                item,
                author,
                LocalDateTime.now()
        );
    }
}
