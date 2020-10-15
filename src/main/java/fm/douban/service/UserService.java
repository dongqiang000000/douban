package fm.douban.service;

import fm.douban.model.User;

public interface UserService {

    User add(User user);

    User get(String name);
}
