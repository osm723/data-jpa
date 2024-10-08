package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MemberQueryRepository memberQueryRepository;

    @Autowired
    EntityManager em;

    @Test
    public void testMember() {
        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        Member member = new Member("MemberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void crudTestMember() {

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //findMember1.setUsername("memeber11");

        // 리스트 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findByUsernameAndAgeGreaterThan("member2", 15);

        assertThat(findMember.get(0).getUsername()).isEqualTo("member2");
        assertThat(findMember.get(0).getAge()).isEqualTo(20);
        assertThat(findMember.size()).isEqualTo(1);
    }

    @Test
    public void findTop3TestBy() {
        List<Member> top3MemberTestBy = memberRepository.findTop3TestBy();
    }

    @Test
    public void findByUsername() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findByUsername("member2");

        assertThat(findMember.get(0)).isEqualTo(member2);
        assertThat(findMember.get(0).getUsername()).isEqualTo("member2");
        assertThat(findMember.size()).isEqualTo(1);
    }

    @Test
    public void findUser() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findUser("member1", 10);

        assertThat(findMember.get(0)).isEqualTo(member1);
        assertThat(findMember.get(0).getUsername()).isEqualTo("member1");
        assertThat(findMember.get(0).getAge()).isEqualTo(10);
        assertThat(findMember.size()).isEqualTo(1);
    }
    
    @Test
    public void findByUsernameList() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findByUsernameList();
        for (String username : usernameList) {
            System.out.println("username = " + username);
        }
    }

    @Test
    public void findMemberDto() {
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");

        Member member1 = new Member("member1", 10);
        member1.setTeam(team1);
        Member member2 = new Member("member2", 20);
        member2.setTeam(team2);

        memberRepository.save(member1);
        memberRepository.save(member2);

        teamRepository.save(team1);
        teamRepository.save(team2);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNames() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findNames = memberRepository.findByNames(Arrays.asList("member1", "member2"));
        for (Member findName : findNames) {
            System.out.println("findName = " + findName);
        }
    }

    @Test
    public void returnType() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> memberListByUsername = memberRepository.findMemberListByUsername("member1");
        for (Member member : memberListByUsername) {
            System.out.println("member = " + member);
        }

        Member memberByUsername = memberRepository.findMemberByUsername("member1");
        System.out.println("memberByUsername = " + memberByUsername);

        Optional<Member> memberOptionalByUsername = memberRepository.findMemberOptionalByUsername("member1");
        System.out.println("memberOptionalByUsername = " + memberOptionalByUsername.get());
    }

    @Test
    public void findByPage() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 10);
        Member member3 = new Member("member3", 10);
        Member member4 = new Member("member4", 10);
        Member member5 = new Member("member5", 10);
        Member member6 = new Member("member6", 20);
        Member member7 = new Member("member7", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);
        memberRepository.save(member7);

        int age = 10;

        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));
        // page로 반환
        Page<Member> memberPage = memberRepository.findByAge(age, pageRequest);
        for (Member member : memberPage) {
            System.out.println("member = " + member);
        }

        assertThat(memberPage.getContent().size()).isEqualTo(3);
        assertThat(memberPage.getTotalElements()).isEqualTo(5);
        assertThat(memberPage.getTotalPages()).isEqualTo(2);
        assertThat(memberPage.hasNext()).isTrue();
        assertThat(memberPage.isFirst()).isTrue();

        // slice로 반환
        Slice<Member> memberSlice = memberRepository.findSliceByAge(age, pageRequest);
        for (Member member : memberSlice) {
            System.out.println("member = " + member);
        }

        assertThat(memberSlice.getContent().size()).isEqualTo(3);
        assertThat(memberSlice.hasNext()).isTrue();
        assertThat(memberSlice.isFirst()).isTrue();

        // list로 반환
        List<Member> memberList = memberRepository.findListByAge(age, pageRequest);
        for (Member member : memberList) {
            System.out.println("member = " + member);
        }

        assertThat(memberList.size()).isEqualTo(3);

        // dto로 변환
        Page<Member> memberPageToDto = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> memberDto = memberPageToDto.map(m -> new MemberDto(m.getId(), m.getUsername(), m.getAge(), null));

        assertThat(memberDto.getContent().size()).isEqualTo(3);
        assertThat(memberDto.getTotalElements()).isEqualTo(5);
        assertThat(memberDto.getTotalPages()).isEqualTo(2);
        assertThat(memberDto.hasNext()).isTrue();
        assertThat(memberDto.isFirst()).isTrue();
    }

    @Test
    public void bulkAgePlus() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 20));
        memberRepository.save(new Member("member3", 25));
        memberRepository.save(new Member("member4", 30));
        memberRepository.save(new Member("member5", 35));
        memberRepository.save(new Member("member6", 40));
        memberRepository.save(new Member("member7", 50));

        int result = memberRepository.bulkAgePlus(20);
        //em.clear();

        assertThat(result).isEqualTo(5);

        List<Member> memberList = memberRepository.findByUsername("member7");

        assertThat(memberList.get(0).getAge()).isEqualTo(51);
        System.out.println("memberList.get(0).getAge() = " + memberList.get(0).getAge());
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //List<Member> memberList = memberRepository.findAll();
        //List<Member> memberList = memberRepository.findMemberFetchJoin();
        //List<Member> memberList = memberRepository.findMemberEntityGraph();
        List<Member> memberList = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : memberList) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void hint() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //Member findMember = memberRepository.findMemberByUsername("member1");
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member3");
    }

    @Test
    public void lock() {
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findLockByUsername("member1");
    }


    @Test
    public void custom() {
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @Test
    public void query() {
        List<Member> memberQuery = memberQueryRepository.findMemberQuery();
    }

    @Test
    public void projections() {
        Team team = new Team("teamA");
        em.persist(team);

        Member member1 = new Member("memberP1", 10, team);
        Member member2 = new Member("memberP2", 20, team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<UsernameOnly> result = memberRepository.findProjectonsByUsername("memberP1");
        for (UsernameOnly usernameOnly : result) {
            System.out.println("getUsername = " + usernameOnly.getUsername());
            System.out.println("getData = " + usernameOnly.getData());
        }
    }

    @Test
    public void projectionsDto() {
        Team team = new Team("teamA");
        em.persist(team);

        Member member1 = new Member("memberP1", 10, team);
        Member member2 = new Member("memberP2", 20, team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<UsernameOnlyDto> result = memberRepository.findProjectonsDtoByUsername("memberP1");
        for (UsernameOnlyDto usernameOnlyDto : result) {
            System.out.println("usernameOnlyDto = " + usernameOnlyDto.getUsername());
        }

        List<UsernameOnlyDto> result2 = memberRepository.findProjectonsDtoByUsername("memberP1", UsernameOnlyDto.class);
        for (UsernameOnlyDto usernameOnlyDto : result2) {
            System.out.println("usernameOnlyDto2 = " + usernameOnlyDto.getUsername());
        }
    }

    @Test
    public void nestedProjections() {
        Team team = new Team("teamA");
        em.persist(team);

        Member member1 = new Member("memberP1", 10, team);
        Member member2 = new Member("memberP2", 20, team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<NestedClosedProjections> result = memberRepository.findProjectonsDtoByUsername("memberP1", NestedClosedProjections.class);
        for (NestedClosedProjections nestedClosedProjections : result) {
            nestedClosedProjections.getUsername();
            System.out.println("getUsername = " + nestedClosedProjections.getUsername());
            nestedClosedProjections.getTeam().getName();
            System.out.println("nestedClosedProjections.gerTeam().getName() = " + nestedClosedProjections.getTeam().getName());
        }
    }

    @Test
    public void nativeQuery() {
        Team team = new Team("teamA");
        em.persist(team);

        Member member1 = new Member("memberP1", 10, team);
        Member member2 = new Member("memberP2", 20, team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findByNavtiveQuery("memberP1");
        System.out.println("findMember.getUsername() = " + findMember.getUsername());
        System.out.println("findMember.getAge() = " + findMember.getAge());
        System.out.println("findMember.getTeam().getName() = " + findMember.getTeam().getName());
    }

    @Test
    public void nativeProjectionQuery() {
        Team team = new Team("teamA");
        em.persist(team);

        Member member1 = new Member("memberP1", 10, team);
        Member member2 = new Member("memberP2", 20, team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        Page<MemberProjection> memberProjections = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        memberProjections.getContent();
        for (MemberProjection memberProjection : memberProjections) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getName() = " + memberProjection.getName());
        }
    }


}