package com.csye6225.spring2019.repository;

import com.csye6225.spring2019.entity.Account;
import com.csye6225.spring2019.entity.Note;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//import javax.transaction.Transactional;
import java.util.List;


@Repository
@Mapper
public interface UserRepository {

    Account findByEmailAddress(@Param("emailAddress") String emailAddress);


//    @Query(value="select * from user where email_address = ?1 and pwdString = ?2",nativeQuery = true)
//    @Transactional
    Account queryAccountByInfo(@Param("emailAddress") String emailAddress,@Param("pwdString") String pwdString);

    int insertAccount(Account account);

    List<Account> findAll();

}
