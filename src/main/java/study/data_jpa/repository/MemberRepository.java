package study.data_jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    public List<Member> findTop3TestBy();

    //@Query(name = "Member.findByUsername")
    public List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    public List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    public List<String> findByUsernameList();

    @Query("select new study.data_jpa.dto.MemberDto(m.id, m.username, m.age, t.name) from Member m join m.team t")
    public List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    public List<Member> findByNames(@Param("names") Collection<String> names);

    public List<Member> findMemberListByUsername(String username);

    public Member findMemberByUsername(String username);

    public Optional<Member> findMemberOptionalByUsername(String username);

    @Query(value = "select m from Member m where m.age = :age", countQuery = "select count(m) from Member m where m.age = :age")
    Page<Member> findByAge(@Param("age") int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    List<Member> findListByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age+1 where m.age > :age")
    int bulkAgePlus(@Param("age") int age);
}
