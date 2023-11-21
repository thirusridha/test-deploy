package com.example.demo.controller;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Dao.BookingRepo;
import com.example.demo.Dao.UserDao;
import com.example.demo.entity.Bookings;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import jakarta.mail.internet.ParseException;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthenticationController {
	private final UserDao empDao;
	private final UserService service;
	private final AuthenticationService authenticationService;
	private final BookingRepo bookingRepo;
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody User request) {
		return ResponseEntity.ok(authenticationService.register(request));
	}
	@GetMapping("/main")		
	public void save1() {
		System.out.println("entered into main...");
	}
	@GetMapping("/getUserBookingList/{id}")
	public Object getUserBookingList(@PathVariable Long id) throws JsonProcessingException {
		List<Bookings> bookingList = bookingRepo.findByUserUserId(id);
		List<User> userList=empDao.findByUserId(Integer.parseInt(id.toString()));
		System.out.println(userList);
		List<Bookings> personList = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		if (!bookingList.isEmpty()) {
			Gson gsnon = new Gson();
			String jsonString = gsnon.toJson(personList);
			System.out.println(bookingList);
			map.put("success", true);
			map.put("data", bookingList);
		} else
			map.put("success", false);
		return ResponseEntity.ok(map);

		
//		List<Bookings> bookingList = bookingRepo.findByUserUserId(id);	
//		HashMap<String, Object> map = new HashMap<>();
//		Integer num = 1;
//		if(!bookingList.isEmpty()) {
//			map.put("success", true);
//			for(Bookings originalBooking:bookingList) {
//				map.put(num.toString(),originalBooking.getDate()+" "+originalBooking.getTimings());
//				num++;
//			};
//		}else
//		{
//			map.put("success", false);
//		}a
//		
//		return ResponseEntity.ok(map);
	}

	@GetMapping("/getDate/{id}/{date}")
	public ResponseEntity<Object> getDate(@PathVariable Integer id, @PathVariable Date date) throws ParseException {
		List<Bookings> dateList = bookingRepo.findByDate(date);
		ArrayList<String> map = new ArrayList<String>();
		Integer presentTime = new Date().getHours();
		System.out.println(presentTime);      
		Integer fromTime;
		LocalDate currentDate = LocalDate.now();
		Instant instant = date.toInstant();
		LocalDate userDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
		if (currentDate.equals(userDate)) {
			fromTime = presentTime + 1;
		} else
			fromTime = 6;
		while (fromTime != 18) {
			Integer toTime = fromTime + 1;
			if (fromTime / 10 < 1 && toTime < 10) {
				map.add("0" + fromTime.toString() + ":00 To 0" + toTime.toString() + ":00");
			} else if (fromTime == 9) {
				map.add("0" + fromTime.toString() + ":00 To " + toTime.toString() + ":00");
			} else
				map.add(fromTime.toString() + ":00 To " + toTime.toString() + ":00");
			fromTime++;
		}
		String allTimings = "";
		String[] timings;
		if (!dateList.isEmpty()) {
			for (Bookings dates : dateList) {
				allTimings = allTimings.concat(dates.getTimings() + ",");
			}
			timings = allTimings.split(",");
			for (String dbTimes : timings) {
				for (String m : map) {
					if (dbTimes.equals(m)) {
						map.remove(m);
						break;
					}
				}
			}
		}
		if (!map.isEmpty()) {
			return ResponseEntity.ok(map);
		} else
			return ResponseEntity.ok(null);
	}

	@GetMapping("/registerConfirmationMail/{email}")
	public void confirmatinMail(@PathVariable String email) {
		service.registerConfirmationMail(email, "Your Registration have been completed...Thank you!");
	}

	@PostMapping("/saveBookings/{id}")
	public ResponseEntity<String> saveBokkings(@PathVariable Integer id, @RequestBody Bookings bookingsObj) {
		List<User> user = empDao.findByUserId(id);
		Bookings bkg = new Bookings();
		List<Bookings> bookingRecord = bookingRepo.findByDateAndUser(bookingsObj.getDate(), user.get(0));
		if (!bookingRecord.isEmpty()) {

			bkg = bookingRecord.get(0);
			String dbTimings = bookingRecord.get(0).getTimings();
			String mergeTimings = dbTimings.concat("," + bookingsObj.getTimings());
			bkg.setTimings(mergeTimings);
			bookingRepo.save(bkg);
		} else {
			bkg.setDate(bookingsObj.getDate());
			bkg.setTimings(bookingsObj.getTimings());
			bkg.setUser(user.get(0));
			bookingRepo.save(bkg);
		}
		return null;
	}

	@PostMapping("/checkUsername")
	public ResponseEntity<Object> checkUserName(@RequestBody User emp) {
		Optional<User> str = empDao.findByUsername(emp.getUsername());
		HashMap<String, Object> map = new HashMap<>();
		System.out.println(str);
		if (str.isPresent()) {
			HashMap<String, Object> registerObj = new HashMap<>();
			registerObj.put("firstName", str.get().getFirstName());
			registerObj.put("lastName", str.get().getLastName());
			registerObj.put("username", str.get().getUsername());
			map.put("success", true);
			map.put("data", registerObj);
			return ResponseEntity.ok(map);
		} else
			map.put("success", false);
		return ResponseEntity.ok(map);
	}

	@PostMapping("/loginConfirm")
	public Object loginConfirm(@RequestBody AuthenticationRequest request) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<User> dbData = empDao.findByUsername(request.getUsername());
		HashMap<String, Object> map = new HashMap<>();
		if (dbData.isPresent()) {
			boolean pswdCheck = encoder.matches(request.getPassword(), dbData.get().getPassword());
			if (pswdCheck) {
				AuthenticationResponse token = authenticationService.authenticate(request);
				map.put("success", true);
				map.put("data", token);
				return ResponseEntity.ok(map);
			} else
				map.put("success", false);
			return ResponseEntity.ok(map);
		} else
			map.put("success", false);
		return ResponseEntity.ok(map);
	}
}