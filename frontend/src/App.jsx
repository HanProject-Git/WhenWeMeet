import { Routes, Route, useNavigate, useParams, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import "./App.css";
import FullCalendar from "@fullcalendar/react";
import dayGridPlugin from "@fullcalendar/daygrid";
import interactionPlugin from "@fullcalendar/interaction";

function Navbar() {
  const [member, setMember] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/api/auth/me", {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) {
          return null;
        }
        return res.json();
      })
      .then((data) => {
        setMember(data);
      })
      .catch(() => {});
  }, []);

  const logout = async () => {
    await fetch("http://localhost:8080/api/auth/logout", {
      method: "POST",
      credentials: "include",
    });

    window.location.href = "/";
  };

  return (
    <nav className="navbar">
      <Link to="/" className="nav-logo">
        WhenWeMeet
      </Link>

      <div className="nav-menu">
        <Link to="/">방 만들기</Link>

        {!member && (
          <>
            <Link to="/login">로그인</Link>
            <Link to="/signup">회원가입</Link>
          </>
        )}

        {member && (
          <>
            <Link to="/mypage">마이페이지</Link>
            <span>{member.name}님</span>
            <button onClick={logout}>로그아웃</button>
          </>
        )}
      </div>
    </nav>
  );
}

function LoginPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    loginId: "",
    password: "",
  });

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const login = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        alert("아이디 또는 비밀번호가 올바르지 않습니다.");
        return;
      }

      alert("로그인 성공!");
      navigate("/");
      window.location.reload();
    } catch (err) {
      alert("로그인 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h1>로그인</h1>

        <label>아이디</label>
        <input name="loginId" value={form.loginId} onChange={handleChange} />

        <label>비밀번호</label>
        <input
          type="password"
          name="password"
          value={form.password}
          onChange={handleChange}
        />

        <button onClick={login}>로그인</button>
        <div className="social-login-box">
        <p>또는</p>

        <button
          type="button"
          onClick={() => {
            window.location.href =
              "http://localhost:8080/oauth2/authorization/google";
          }}
        >
          Google로 로그인
        </button>
        </div>
      </div>
    </div>
  );
}

function SignupPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    loginId: "",
    password: "",
    name: "",
  });

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const signup = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/auth/signup", {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        alert("회원가입 실패");
        return;
      }

      alert("회원가입 성공!");
      navigate("/login");
    } catch (err) {
      alert("회원가입 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h1>회원가입</h1>

        <label>이름</label>
        <input name="name" value={form.name} onChange={handleChange} />

        <label>아이디</label>
        <input name="loginId" value={form.loginId} onChange={handleChange} />

        <label>비밀번호</label>
        <input
          type="password"
          name="password"
          value={form.password}
          onChange={handleChange}
        />

        <button onClick={signup}>회원가입</button>
      </div>
    </div>
  );
}

function HomePage() {
  const navigate = useNavigate();

  const [inviteCode, setInviteCode] = useState("");
  const [form, setForm] = useState({
    title: "",
    description: "",
    startDate: "",
    endDate: "",
  });
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const createRoom = async () => {
    setError("");

    try {
      const response = await fetch("http://localhost:8080/api/rooms", {
        method: "POST",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(form),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "방 생성 실패");
      }

      const roomId = await response.json();

      alert("방 생성 완료!");
      navigate(`/rooms/${roomId}`);
    } catch (err) {
      setError(err.message);
    }
  };

  const enterByInviteCode = async () => {
    if (!inviteCode.trim()) {
      alert("초대코드를 입력해주세요.");
      return;
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/rooms/invite/${inviteCode.trim()}`,
        {
          credentials: "include",
        }
      );

      if (!response.ok) {
        alert("존재하지 않는 초대코드입니다.");
        return;
      }

      navigate(`/invite/${inviteCode.trim()}`);
    } catch (err) {
      alert("초대코드 확인 중 오류가 발생했습니다.");
    }
  };

  return (
    <div className="container">
      <h1>WhenWeMeet</h1>
      <p>팀 일정 조율방 만들기</p>

      <div className="card">
        <h2>새 일정방 만들기</h2>

        <label>제목</label>
        <input name="title" value={form.title} onChange={handleChange} />

        <label>설명</label>
        <textarea
          name="description"
          value={form.description}
          onChange={handleChange}
        />

        <label>시작일</label>
        <input
          type="date"
          name="startDate"
          value={form.startDate}
          onChange={handleChange}
        />

        <label>종료일</label>
        <input
          type="date"
          name="endDate"
          value={form.endDate}
          onChange={handleChange}
        />

        <button onClick={createRoom}>방 만들기</button>
      </div>

      <div className="card">
        <h2>초대코드로 참여하기</h2>

        <input
          value={inviteCode}
          onChange={(e) => setInviteCode(e.target.value)}
          placeholder="초대코드를 입력하세요"
        />

        <button onClick={enterByInviteCode}>참여하러 가기</button>
      </div>

      {error && <div className="error">{error}</div>}
    </div>
  );
}
function MyPage() {
  const navigate = useNavigate();

  const [ownedRooms, setOwnedRooms, ] = useState([]);
  const [member, setMember] = useState(null);

  useEffect(() => {

    fetch("http://localhost:8080/api/auth/me", {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setMember(data));

    fetch("http://localhost:8080/api/mypage/rooms/owned", {
      credentials: "include",
    })
      .then(async (res) => {
        if (!res.ok) {
          const errorData = await res.json();
          throw new Error(errorData.message || "마이페이지 조회 실패");
        }

        return res.json();
      })
      .then((data) => setOwnedRooms(data))
      .catch((err) => {
        alert(err.message);
      });

  }, []);

  return (
    <div className="container">
      <h1>마이페이지</h1>
          {member && (
      <div className="card">
        <h2>내 정보</h2>

        <p>이름: {member.name}</p>

        <p>
          아이디/이메일: {member.loginId}
        </p>

        <p>
          로그인 방식:
          {" "}
          {member.provider === "GOOGLE"
            ? "Google 소셜 로그인"
            : "일반 로그인"}
        </p>

        <p>
          가입일:
          {" "}
          {member.createdAt?.replace("T", " ")}
        </p>
        <button
          onClick={async () => {

            const confirmed = window.confirm(
              "정말 회원탈퇴 하시겠습니까?"
            );

            if (!confirmed) {
              return;
            }

            try {
              const response = await fetch(
                "http://localhost:8080/api/auth/me",
                {
                  method: "DELETE",
                  credentials: "include",
                }
              );

              if (!response.ok) {
                alert("회원탈퇴 실패");
                return;
              }

              alert("회원탈퇴 완료");

              window.location.href = "/";
            } catch (err) {
              alert("회원탈퇴 중 오류가 발생했습니다.");
            }
          }}
        >
          회원탈퇴
        </button>
      </div>
    )}

      <div className="card">
        <h2>내가 만든 일정방</h2>

        {ownedRooms.length === 0 ? (
          <p>아직 만든 일정방이 없습니다.</p>
        ) : (
          <div className="date-grid">
            {ownedRooms.map((room) => (
              <div className="date-card" key={room.id}>
                <div className="date">{room.title}</div>
                <div className="count">
                  {room.startDate} ~ {room.endDate}
                </div>

                <button onClick={() => navigate(`/rooms/${room.id}`)}>
                  상세보기
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

function RoomPage() {
  const navigate = useNavigate();
  const { roomId } = useParams();

  const [room, setRoom] = useState(null);
  const [dateCounts, setDateCounts] = useState([]);
  const [commonDates, setCommonDates] = useState([]);
  const [participants, setParticipants] = useState([]);

  useEffect(() => {
    fetch(`http://localhost:8080/api/rooms/${roomId}`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setRoom(data));

    fetch(`http://localhost:8080/api/rooms/${roomId}/result`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setDateCounts(data));

    fetch(`http://localhost:8080/api/rooms/${roomId}/result/common`, {
      credentials: "include",
    })
      .then((res) => res.json())
      .then((data) => setCommonDates(data));

    fetch(`http://localhost:8080/api/rooms/${roomId}/participants`, {
      credentials: "include",
    })
      .then(async (res) => {
        if (!res.ok) {
          const text = await res.text();
          console.error("참여자 명단 조회 실패:", text);
          return [];
        }

        const data = await res.json();
        return Array.isArray(data) ? data : [];
      })
      .then((data) => setParticipants(data))
      .catch((err) => {
        console.error("참여자 명단 요청 오류:", err);
        setParticipants([]);
      });
  }, [roomId]);

  if (!room) {
    return <div className="container">불러오는 중...</div>;
  }

  const inviteUrl = `${window.location.origin}/invite/${room.inviteCode}`;

  const resultEvents = dateCounts.map((item) => ({
    title: `${item.participants.length}명 가능`,
    date: item.date,
  }));

  return (
    <div className="container">
      <h1>{room.title}</h1>

      <div className="card">
        <p>{room.description}</p>

        <p>
          기간: {room.startDate} ~ {room.endDate}
        </p>

        <p>초대코드</p>
        <input value={room.inviteCode} readOnly />

        <p>초대 링크</p>
        <input value={inviteUrl} readOnly />

        <button onClick={() => navigate(`/invite/${room.inviteCode}`)}>
          내 가능 날짜 수정하기
        </button>
      </div>

      <div className="card">
        <h2>방 참여자 명단</h2>

        {participants.length === 0 ? (
          <p>아직 참여자가 없습니다.</p>
        ) : (
          <div className="date-grid">
            {participants.map((participant) => (
              <div className="date-card" key={participant.id}>
                <div className="date">{participant.name}</div>
              </div>
            ))}
          </div>
        )}
      </div>

      <div className="card">
        <h2>일정 결과 캘린더</h2>

        <FullCalendar
          plugins={[dayGridPlugin, interactionPlugin]}
          initialView="dayGridMonth"
          initialDate={room.startDate}
          events={resultEvents}
          height="auto"
        />
      </div>

      <div className="card">
        <h2>모두 가능한 날짜</h2>

        {commonDates.length === 0 ? (
          <p>아직 모두 가능한 날짜가 없습니다.</p>
        ) : (
          <div className="date-grid">
            {commonDates.map((date) => {
              const matched = dateCounts.find((item) => item.date === date);
              const dateParticipants = matched?.participants || [];

              return (
                <div className="date-card" key={date}>
                  <div className="date">{date}</div>

                  <div className="participants">
                    참여자: {dateParticipants.join(", ")}
                  </div>

                  <div className="count">{dateParticipants.length}명</div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}

function InvitePage() {
  const navigate = useNavigate();
  const { inviteCode } = useParams();

  const [room, setRoom] = useState(null);
  const [participantId, setParticipantId] = useState(null);
  const [selectedDates, setSelectedDates] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    fetch(`http://localhost:8080/api/rooms/invite/${inviteCode}`, {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error("방 정보를 찾을 수 없습니다.");
        }
        return res.json();
      })
      .then((data) => {
        setRoom(data);

        return fetch(
          `http://localhost:8080/api/rooms/${data.id}/participants/me`,
          {
            credentials: "include",
          }
        );
      })
      .then((res) => {
        if (!res.ok) {
          return null;
        }
        return res.json();
      })
      .then((data) => {
        if (data) {
          setParticipantId(data);

          return fetch(
            `http://localhost:8080/api/participants/${data}/available-dates`,
            {
              credentials: "include",
            }
          );
        }

        return null;
      })
      .then((res) => {
        if (!res) {
          return null;
        }

        return res.json();
      })
      .then((dates) => {
        if (dates) {
          setSelectedDates(dates);
        }
      })
      .catch((err) => {
        setError(err.message);
      });
  }, [inviteCode]);

  const joinRoom = async () => {
    setError("");

    try {
      const response = await fetch(
        `http://localhost:8080/api/rooms/${room.id}/participants`,
        {
          method: "POST",
          credentials: "include",
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "참여 실패");
      }

      const id = await response.json();

      setParticipantId(id);
      alert("참여 완료!");
    } catch (err) {
      alert(err.message);
    }
  };

  const handleDateClick = (info) => {
    const clickedDate = info.dateStr;

    if (selectedDates.includes(clickedDate)) {
      setSelectedDates(selectedDates.filter((date) => date !== clickedDate));
    } else {
      setSelectedDates([...selectedDates, clickedDate]);
    }
  };

  const saveAvailableDates = async () => {
    try {
      const response = await fetch(
        `http://localhost:8080/api/participants/${participantId}/available-dates`,
        {
          method: "POST",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            dates: selectedDates,
          }),
        }
      );

      if (!response.ok) {
        alert("가능 날짜 저장 실패");
        return;
      }

      alert("가능 날짜 저장 완료!");
      navigate(`/rooms/${room.id}`);
    } catch (err) {
      alert("가능 날짜 저장 중 오류가 발생했습니다.");
    }
  };

  if (error) {
    return (
      <div className="container">
        <div className="error">{error}</div>
      </div>
    );
  }

  if (!room) {
    return <div className="container">불러오는 중...</div>;
  }

  const events = selectedDates.map((date) => ({
    title: "가능",
    date,
  }));

  return (
    <div className="container">
      <h1>{room.title}</h1>
      <p>{room.description}</p>

      <div className="card">
        <p>
          기간: {room.startDate} ~ {room.endDate}
        </p>

        {!participantId && (
          <>
            <p>로그인한 계정으로 이 일정에 참여합니다.</p>
            <button onClick={joinRoom}>참여하기</button>
          </>
        )}

        {participantId && (
          <>
            <p>가능한 날짜를 클릭해서 수정하세요.</p>

            <FullCalendar
              plugins={[dayGridPlugin, interactionPlugin]}
              initialView="dayGridMonth"
              initialDate={room.startDate}
              dateClick={handleDateClick}
              events={events}
              height="auto"
            />

            <button onClick={saveAvailableDates}>가능 날짜 저장</button>
          </>
        )}
      </div>
    </div>
  );
}

function App() {
  return (
    <>
      <Navbar />

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/rooms/:roomId" element={<RoomPage />} />
        <Route path="/invite/:inviteCode" element={<InvitePage />} />
        <Route path="/mypage" element={<MyPage />} />
      </Routes>
    </>
  );
}

export default App;