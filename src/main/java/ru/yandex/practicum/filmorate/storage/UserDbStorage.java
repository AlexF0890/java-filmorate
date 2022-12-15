package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate users;
    private final UserMapper userMapper;

    @Override
    public User findById(Integer id) {
        String sqlQuery = "select * from users where user_id = ?";
        try {
            return users.queryForObject(sqlQuery, userMapper, id);
        } catch (EmptyResultDataAccessException e){
            throw new UserNotFoundException("Пользователя не существует");
        }
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into users (login, user_name, email, birthday) " +
                "values (?, ?, ?, ?)";
        if (user.getName() == null) {
           user.setName(user.getLogin());
        }
            KeyHolder keyHolder = new GeneratedKeyHolder();
            users.update (connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
                stmt.setString(1, user.getLogin());
                stmt.setString(2, user.getName());
                stmt.setString(3, user.getEmail());
                stmt.setDate(4, Date.valueOf(user.getBirthday()));
                return stmt;
            }, keyHolder);
            user.setId((keyHolder.getKey()).intValue());
            return user;
    }

    @Override
    public User update(User user) {
        if(findById(user.getId()) != null) {
            String sqlQuery = "update users set " +
                    "login = ?, user_name = ?, email = ?, birthday = ?" +
                    "where user_id = ?";
            users.update(sqlQuery,
                    user.getLogin(),
                    user.getName(),
                    user.getEmail(),
                    user.getBirthday(),
                    user.getId());
            return user;
        } else {
            throw new UserNotFoundException("пользователя не существует");
        }
    }

    @Override
    public void remove(User user) {
        String sqlQuery = "delete from users where user_id = ?";
        users.update(sqlQuery, user.getId());
    }

    @Override
    public Collection<User> getUsers() {
        return users.query("select * from users", userMapper);
    }

    @Override
    public void addFriendList(Integer user, Integer friend) {
        String sqlQuery = "insert into friend (user_id, friend_id) values (?, ?)";
        users.update(sqlQuery, user, friend);
    }

    @Override
    public void removeFriend(Integer user, Integer friend) {
        String sqlQuery = "delete from friend where user_id = ? and friend_id = ?";
        users.update(sqlQuery, user, friend);
    }

    public Boolean isMutualStatus (Integer user, Integer friend) {
        String sqlQuery = "select (select user_id from friend where user_id = ? and friend_id = ? ) " +
                "and " +
                "(select user_id from friend where user_id = ? and friend_id = ? ) " +
                "as is_mutual";
        return users.query(sqlQuery, (rs, romNum) -> rs.getObject("is_mutual", Boolean.class),
                user, friend, friend, user).stream().anyMatch(Objects::nonNull);
    }

    @Override
    public List<User> getMutualFriend(Integer user, Integer friend) {
        List<User> userCommon = new ArrayList<>();
        String sqlQuery = "select users.* from users " +
                "inner join friend as u on users.user_id = u.friend_id " +
                "inner join friend as f on users.user_id = f.friend_id " +
                "where u.user_id = ? and f.user_id = ?";
        users.query(sqlQuery, (rs,rowNum) ->
                userCommon.add(userMapper.mapRow(rs, rowNum)), user, friend);
        return new ArrayList<>(userCommon);
    }

    @Override
    public List<User> getFriendsList(Integer user) {
        String sqlQuery = "select users.* from friend " +
                "join users on users.user_id = friend.friend_id " +
                "where friend.user_id = ?";
        return users.query(sqlQuery, userMapper, user);
    }
}
