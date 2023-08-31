package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Builder
public class MockPostRepository implements PostRepository {
    private Long generatedId = 0L;
    private final List<Post> data = new ArrayList<>();
    @Override
    public Optional<Post> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Post save(Post post) {
        if(post.getId() == null || post.getId() == 0){
            Post newPost = Post.builder()
                    .id(generatedId++)
                    .content(post.getContent())
                    .modifiedAt(post.getModifiedAt())
                    .writer(post.getWriter())
                    .build();
            data.add(newPost);
            return newPost;

        }else{
            data.removeIf(item -> Objects.equals(item.getId(), post.getId()));
            data.add(post);
            return post;
        }
    }
}
