package reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import coninfo.conInfo_vo.ConInfoVO;
import coninfo.dao.ConInfoDAO;
import hall_info.Hall_infoDAO;
import hall_info.Hall_infoVO;
import pay.dao.PayDao;
import pay.vo.Pay_VO;
import seat_info.Seat_infoDAO;
import seat_info.Seat_infoVO;
import users.UsersDAO;
import users.UsersVO;

public class Reservation {
    public void booking() {
        Scanner scan = new Scanner(System.in);

    	ReservationDAO reservationDAO = new ReservationDAO(); //reservation 테이블 용 메소드
    	ConInfoDAO conInfoDAO = new ConInfoDAO(); //concert_info 테이블 용 메소드
    	UsersDAO usersDAO = new UsersDAO(); //users 테이블 용 메소드
    	Hall_infoDAO hallInfoDAO = new Hall_infoDAO(); //users 테이블 용 메소드
    	Seat_infoDAO seatInfoDAO = new Seat_infoDAO(); //users 테이블 용 메소드
    	
    	List<ReservationVO> rlist = reservationDAO.selectAll(); //reservation 테이블 데이터 전체 조회
    	List<ConInfoVO> clist = conInfoDAO.selectAll(); //concert_info 테이블 데이터 전체 조회
    	List<UsersVO> ulist = usersDAO.selectAll(); //users 테이블 데이터 전체 조회
    	List<Hall_infoVO> hlist = hallInfoDAO.selectAll(); //hall_info 테이블 데이터 전체 조회
    	List<Seat_infoVO> slist = seatInfoDAO.selectAll(); //seat_info 테이블 데이터 전체 조회
//    	List<ReservationVO> hrlist = reservationDAO.HashmapselectAll(); //reservation 테이블 데이터 전체 조회

    	
    	int select; //메인 메뉴에서 입력한 번호 값
    	LocalDate nowDate = LocalDate.now(); //현재 날짜, 시간 입력할 때 사용할 변수

    	int intscanner; //숫자 스캐너
    	String sscanner; //문자열 스캐너
    	
    	//예매하기 사용 변수-----------------------------------------------------------
    	int book_id;
    	String user_id = null;
    	int concert_id;
    	int hall_id;
    	int seat_id;
    	int count;
    	String seat = null;
    	int totalprice;
    	String paymentMethod = null;
    	String createDate = nowDate.toString();
    	String status = "예매완료";
    	
    	String concertscanner; //예매하기 - 콘서트 번호 입력 값
    	int countscanner; //예매하기 - 콘서트 예매 인원 수 값
    	int seatscanner; //예매하기 - 예매할 좌석 수 값
    	String paymentMethodscanner; //예매하기 - 결제방법 번호 입력 값

    	HashMap<Integer, String> seatall = new HashMap<>();
    	
    	for (Seat_infoVO vo : slist) {
    		seatall.put(vo.getSeat_id(), vo.getSeat_no());
    	}
//    	System.out.println(seatall); //여기 해시맵에 좌석id, 좌석번호 일단 다 넣고

    	HashMap<Integer, String> seatcheck = new HashMap<>();
//    	for (ReservationVO vo : hrlist) {
//    		seatcheck.put(vo.getSeat_id(), vo.getSeat1());
//    	}
//    	System.out.println(seatcheck); //여기 해시맵에는 예약된 좌석id, 좌석 번호 다 넣기
    	

    	System.out.println();
//    	for (ReservationVO vo : hrlist) {
//    		 System.out.println(vo);
//    		 seatcheck.put(vo.getSeat_id(), vo.getSeat1());
//    		 seatcheck.put(vo.getSeat_id(), vo.getSeat2());
//    		 seatcheck.put(vo.getSeat_id(), vo.getSeat3());
//    		 System.out.println(seatcheck.keySet());
    //
//    		}
    	

	while (true) {
		// 1. 콘서트 선택
		System.out.println();
		System.out.println("<콘서트 INFORMATION>");
		System.out.println("번호\t콘서트명\t\t\t\t장르\t러닝타임\t날짜\t\t\t장소\t\t\t시간");
		for (ConInfoVO vo : clist) {
		    System.out.println(String.format("%-1s\t%-24s\t%-5s\t%-5s\t%-10s\t%-20s\t%-20s",
		                      vo.getConcert_id(), vo.getTitle(), vo.getGenre(),
		                      vo.getRunning_time(), vo.getConcert_date(), vo.getLocation(), 
		                      vo.getTime()));
		}
		System.out.println("---------------------------------------------------------------");
		System.out.println("관람할 콘서트의 번호를 입력하시오.");

		while (true) {
			System.out.print("콘서트 번호 입력 : ");
			concertscanner = scan.nextLine();
			
			if (Integer.parseInt(concertscanner) <= 99) {
                // 입력값이 1부터 10 사이인 경우
                break; // 올바른 입력이므로 반복문 종료
            } else {
                // 입력값이 범위를 벗어난 경우
                System.out.println("잘못된 입력입니다. 다시 입력하세요.");
            }
		}
		ConInfoVO id = conInfoDAO.selectOne(concertscanner);
		System.out.println();
		
		// 2. 인원 선택하기
		System.out.println("관람할 인원을 입력하시오.(최대 5명)");
		while (true) {
			System.out.print("인원 입력 : ");
			countscanner = scan.nextInt();
			scan.nextLine();
			
			 if (countscanner < 6) {
	                break;
			 } else {
	                System.out.println("인원은 최대 5명까지 가능합니다. 다시 입력해주세요.");
	           }
		}
		System.out.println();

		// 3. 좌석 선택하기-------------------------------------------------------------
		List<Integer> test = new ArrayList<>();
		
		for (Seat_infoVO vo : slist) {
		    if(vo.getHall_id() == id.getHall_id()) {
		    	System.out.println(vo.getSeat_id() + "." + vo.getSeat_no() + " ");
		    	 test.add(vo.getSeat_id()); 
		    }
			
		}

		String[] seatNumbers = new String[5]; // 배열 생성

		System.out.println("원하는 좌석의 번호를 입력하시오.");
			for (int i = 0; i < countscanner; i++) {
				int a = i+1;
			    System.out.print("좌석 번호 입력 " + a + " : ");
			    seatscanner = scan.nextInt();
			    scan.nextLine();

				Seat_infoVO selectseat = seatInfoDAO.selectOne(String.valueOf(seatscanner));

			    if (seatscanner < test.get(0) || seatscanner > test.get(test.size() - 1)) {
			        System.out.println("유효하지 않은 좌석 번호입니다. 다시 입력하세요.");
			        i--; // Retry input for the same index
			        continue;
			    }
			   seatNumbers[i] = selectseat.getSeat_no();
			}
			for (int i = 0; i < seatNumbers.length; i++) {
				if(seatNumbers[i] != null) {
					System.out.print(seatNumbers[i] + " ");
				}
			} System.out.println("좌석을 선택하셨습니다.");
		
		System.out.println();
		System.out.println("---------------------------------------------------------------");

		// 4. 회원/비회원 예매 선택하기
		System.out.println("1.회원 예매 2.비회원 예매");
		System.out.print("번호 입력 : ");
		intscanner = scan.nextInt();
		scan.nextLine();
		System.out.println("---------------------------------------------------------------");
		
		UsersVO user = null;
		
		if(intscanner == 1) {
			Scanner sc = new Scanner(System.in);
			
			while (true) {
	            System.out.print("아이디를 입력하세요 : ");
	             user_id = sc.nextLine();

	            // UsersDAO를 이용하여 데이터베이스에서 사용자 정보 조회
	            usersDAO = new UsersDAO();
	            user = usersDAO.selectOne(user_id);

	            if (user != null) {
	            	while (true) {
	                    System.out.print("비밀번호를 입력하세요: ");
	                    String password = sc.nextLine();

	                    if (user.getPassword().equals(password)) {
	                        System.out.println("로그인에 성공했습니다.");
	                        break;
	                    } else {
	                        System.out.println("비밀번호가 일치하지 않습니다. 다시 입력하세요");
	                    }
	                }
	                break; 
	            } else {
	                System.out.println("일치하는 아이디가 없습니다. 다시 입력하세요");
	            }
	        }
				
			System.out.println();
			System.out.println("---------------------------------------------------------------");
		}
		if(intscanner == 2) {
			System.out.println("<비회원 RESERVATION>");
			while (true) {
				System.out.print("전화번호 입력(010-0000-0000) : ");
				sscanner = scan.nextLine();
				
				if (sscanner.matches("\\d{3}-\\d{4}-\\d{4}")) {
                    break; // 올바른 형식이므로 반복문 종료
                } else {
                    System.out.println("잘못된 형식입니다. 다시 입력하세요.");
                }
			}
			System.out.println();
			System.out.print("티켓수령지 입력 : ");
			sscanner = scan.nextLine();
			
			System.out.println();
			System.out.println("---------------------------------------------------------------");
		}
		

		// 5. 결제하기
		// 결제 정보 입력 받기
		System.out.println("<결제하기 PAYMENT>");
//		System.out.print("사용자 아이디 입력 : ");
//		String userId = scan.nextLine();
		System.out.print("결제 방법 선택 (1.카드결제/2.계좌이체) : ");
		String paymentMethod1 = scan.nextLine();

		// 결제 정보 생성 및 저장
		PayDao paymentDao = new PayDao(); // Pay_Dao 객체 생성
		Pay_VO payment = new Pay_VO(0, user_id, paymentMethod1, 0, createDate, 0); // Pay_VO 객체 생성
		payment.setUser_id(user_id);
		payment.setPayment_method(paymentMethod1); // 변수명을 paymentMethod1로 수정

		// 예약 정보 출력 (예약 번호와 가격)
		System.out.print("예약 번호 입력 : ");
		int reservationId = scan.nextInt();
		scan.nextLine();
		ReservationVO reservation = reservationDAO.selectOne(Integer.toString(reservationId)); // 정수를 문자열로 변환하여 전달

		if (reservation != null) {
		    payment.setTotal_price(reservation.getTotalprice());
		    payment.setPayment_date(createDate);
		    payment.setBook_id(reservationId);

		    // 결제 정보 저장
		    int paymentResult = paymentDao.insert(payment); // paymentDao를 사용하여 insert 메소드 호출
		    if (paymentResult > 0) {
		        System.out.println("결제가 완료되었습니다.");
		    } else {
		        System.out.println("결제에 실패했습니다.");
		    }
		} else {
		    System.out.println("해당 예약 번호의 예약 정보가 없습니다.");
		}
		
		// 6. 예매완료
		System.out.println("성공적으로 예매 되었습니다.");
		System.out.println("추가로 예매를 원하시면 \"Y\"를 원치 않으시면 \"N\"를 입력하세요.");
		while (true) {
			System.out.print("입력 : ");
			sscanner = scan.nextLine();
			if (sscanner.equalsIgnoreCase("Y")) {
				//여기서 Y 누르면 다시 예매 탭으로 가고
				System.out.println("---------------------------------------------------------------");
				break;
			} else if (sscanner.equalsIgnoreCase("N")) {
				//N 누르면 처음으로 돌아가게 하기
				break;
			} else {
            // Y나 N이 아닌 다른 입력인 경우
            System.out.println("잘못된 입력입니다. \"Y\"나 \"N\"을 입력하세요.");
			}
		}
		if (sscanner.equalsIgnoreCase("N")) {
			//N 누르면 처음으로 돌아가게 하기
			System.out.println("감사합니다. 안녕히가시오.");
			System.out.println("---------------------------------------------------------------");
			System.out.println();
			System.out.println();
			break;
		} 
	}
}
    
}

