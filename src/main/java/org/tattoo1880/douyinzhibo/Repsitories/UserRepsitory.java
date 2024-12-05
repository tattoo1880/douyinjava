package org.tattoo1880.douyinzhibo.Repsitories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.tattoo1880.douyinzhibo.Entities.TUser;


@Repository
public interface UserRepsitory extends R2dbcRepository<TUser,Long> {
}
