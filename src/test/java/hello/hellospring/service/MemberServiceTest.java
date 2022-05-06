package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class MemberServiceTest {
    MemberRepository memberRepository = new MemoryMemberRepository();
    MemberService memberService = new MemberService(memberRepository);

    @Test
    void join() {
        Member member1 = new Member();
        member1.setName("spring1");
        memberService.join(member1);

        Member foundMember = memberService.findOne(member1.getId()).get();
        assertThat(foundMember.getName()).isEqualTo(member1.getName());
    }

    @Test
    void duplicatedJoin() {
        //given
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring1");

        //when
        memberService.join(member1);
        try {
            memberService.join(member2);
            fail("IllegalStateException expected");
        }catch (IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원 이름입니다!");
        }

    }

    @Test
    void findMembers() {
        //given
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring2");

        //when
        memberService.join(member1);
        memberService.join(member2);

        assertThat(memberService.findMembers().size()).isEqualTo(2);
    }

    @Test
    void findOne() {
        //given
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring2");

        //when
        memberRepository.save(member1);
        memberRepository.save(member2);
        Member memberFoundById = memberRepository.findById(member1.getId()).get();
        Member memberFoundByName = memberRepository.findByName(member2.getName()).get();
        assertThat(memberFoundById).isEqualTo(member1);
        assertThat(memberFoundByName).isEqualTo(member2);
    }
}