package com.example.demo700.CyclicCleaner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.ENUMS.TeamJoinRequestRole;
import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamJoinRequest;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.AthleteLocation.AthleteLocation;
import com.example.demo700.Models.ChatModels.ChatMessage;
import com.example.demo700.Models.DoctorModels.Doctor;
import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchName;
import com.example.demo700.Models.EventOrganaizer.MatchVenue;
import com.example.demo700.Models.FileUploadModel.CVUploadModel;
import com.example.demo700.Models.FileUploadModel.ProfileIamge;
import com.example.demo700.Models.GymModels.Gyms;
import com.example.demo700.Models.NotificationModels.Notification;
import com.example.demo700.Models.PaymentGateway.BkashTransaction;
import com.example.demo700.Models.TeamLocationModels.TeamLocationModel;
import com.example.demo700.Models.Turf.Booking;
import com.example.demo700.Models.Turf.Discount;
import com.example.demo700.Models.Turf.GroupBooking;
import com.example.demo700.Models.Turf.Owner;
import com.example.demo700.Models.Turf.Venue;
import com.example.demo700.Models.Turf.VenueLocation;
import com.example.demo700.Repositories.UserRepository;
import com.example.demo700.Repositories.Athelete.AtheleteRepository;
import com.example.demo700.Repositories.Athelete.CoachRepository;
import com.example.demo700.Repositories.Athelete.ScoutsRepository;
import com.example.demo700.Repositories.Athelete.TeamJoinRequestRepository;
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.AthleteRepository.AthleteLocationRepository;
import com.example.demo700.Repositories.ChatRepositories.ChatMessageRepository;
import com.example.demo700.Repositories.DoctorRepositories.DoctorRepository;
import com.example.demo700.Repositories.EventOrganaizer.EventOrganaizerRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchNameRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchVenueRepository;
import com.example.demo700.Repositories.FileUploadRepositories.CVUploadRepository;
import com.example.demo700.Repositories.FileUploadRepositories.ProfileImageRepository;
import com.example.demo700.Repositories.GymRepositories.GymsRepository;
import com.example.demo700.Repositories.NotificationRepositories.NotificationRepository;
import com.example.demo700.Repositories.PaymentRepositories.BkashTransactionRepository;
import com.example.demo700.Repositories.TeamLocationRepositories.TeamLocationRepository;
import com.example.demo700.Repositories.Turf.BookingRepository;
import com.example.demo700.Repositories.Turf.DiscountRepository;
import com.example.demo700.Repositories.Turf.GroupBookingRepository;
import com.example.demo700.Repositories.Turf.OwnerRepository;
import com.example.demo700.Repositories.Turf.VenueLocationServiceRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;
import com.example.demo700.Services.FileUploadServices.ImageService;

@Service
public class CyclicCleaner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AtheleteRepository atheleteRepository;

	@Autowired
	private CoachRepository coachRepository;

	@Autowired
	private ScoutsRepository scoutsRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private TeamOwnerRepository teamOwnerRepository;

	@Autowired
	private EventOrganaizerRepository eventOrganaizerRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private DiscountRepository discoutnRepository;

	@Autowired
	private GroupBookingRepository groupBookingRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private VenueRepository venueReposiotry;

	@Autowired
	private VenueLocationServiceRepository venueLocationRepository;

	@Autowired
	private MatchVenueRepository matchVenueRepository;

	@Autowired
	private BkashTransactionRepository bkashTransactionRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Autowired
	private TeamJoinRequestRepository teamJoinRequestRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ProfileImageRepository profileImageRepository;

	@Autowired
	private CVUploadRepository cvUploadRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private AthleteLocationRepository athleteLocationRepository;
	
	@Autowired
	private TeamLocationRepository teamLocationRepository;
	
	@Autowired
	private MatchNameRepository matchNameRepository;

	@Autowired
	private GymsRepository gymsRepository;
	
	public void removeUser(String userId) {

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			long count = userRepository.count();

			userRepository.deleteById(userId);

			if (count != userRepository.count()) {

				try {

					Athelete athelete = atheleteRepository.findByUserId(userId).get();

					if (athelete == null) {

						throw new Exception();

					}

					removeAthelete(athelete.getId());

				} catch (Exception e) {

				}

				try {

					EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(userId);

					if (eventOrganaizer == null) {

						throw new Exception();

					}

					removeEventOrganaizer(eventOrganaizer.getId());

				} catch (Exception e) {

				}

				try {

					List<Booking> list = bookingRepository.findByUserId(userId);

					for (Booking booking : list) {

						if (booking != null) {

							removeBooking(booking.getId());

						}

					}

				} catch (Exception exception) {

				}

				try {

					List<GroupBooking> groupBookings = groupBookingRepository
							.findByMemberIdsContainingIgnoreCase(userId);

					if (!groupBookings.isEmpty()) {

						for (GroupBooking groupBooking : groupBookings) {

							removeGroupBooking(groupBooking.getId());

						}

					}

				} catch (Exception exceptione) {

				}

				try {

					Owner owner = ownerRepository.searchByUserId(userId);

					if (owner != null) {

						removeVenueOwner(owner.getId());

					}

				} catch (Exception e) {

				}

				try {

					List<ChatMessage> chatMessages = chatMessageRepository.findByReceiverOrSender(userId, userId);

					if (!chatMessages.isEmpty()) {

						for (ChatMessage k : chatMessages) {

							removeChatMessage(k.getId());

						}

					}

				} catch (Exception e) {

				}

				try {

					List<Notification> notifications = notificationRepository.findByUserId(userId);

					if (!notifications.isEmpty()) {

						for (Notification k : notifications) {

							removeNotification(k.getId());

						}

					}

				} catch (Exception e) {

				}

				try {

					List<BkashTransaction> list = bkashTransactionRepository.findBySenderId(userId);

					if (!list.isEmpty()) {

						for (BkashTransaction i : list) {

							removeBikashTransaction(i.getId());

						}

					}

				} catch (Exception e) {

				}

				try {

					List<BkashTransaction> list = bkashTransactionRepository.findByReceiverId(userId);

					if (!list.isEmpty()) {

						for (BkashTransaction i : list) {

							removeBikashTransaction(i.getId());

						}

					}

				} catch (Exception e) {

				}

				try {

					ProfileIamge profileImage = profileImageRepository.findByUserId(user.getId());

					if (profileImage != null) {

						removeProfileImage(profileImage.getId());

					}

				} catch (Exception e) {

				}

				try {

					CVUploadModel model = cvUploadRepository.findByUserId(user.getId());

					if (model != null) {

						removeCV(model.getId());

					}

				} catch (Exception e) {

				}

				try {

					Doctor doctor = doctorRepository.findByUserId(user.getId());

					if (doctor != null) {

						removeDoctor(doctor.getId());

					}

				} catch (Exception e) {

				}

				List<Discount> discounts = discoutnRepository.findByOwnerId(userId);

				if (!discounts.isEmpty()) {

					for (Discount discount : discounts) {

						removeDiscount(discount.getId());

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeAthelete(String athelteId) {

		try {

			Athelete athelete = atheleteRepository.findById(athelteId).get();

			if (athelete == null) {

				throw new Exception();

			}

			long count = atheleteRepository.count();

			atheleteRepository.deleteById(athelteId);

			if (count != atheleteRepository.count()) {

				try {

					Coach coach = coachRepository.findByAtheleteId(athelteId);

					if (coach != null) {

						removeCoach(coach.getId());

					}

				} catch (Exception exceptione) {

				}

				try {

					Scouts scouts = scoutsRepository.findByAtheleteId(athelteId);

					if (scouts != null) {

						removeScout(scouts.getId());

					}

				} catch (Exception e) {

				}

				try {

					TeamOwner teamOwner = teamOwnerRepository.findByAtheleteId(athelteId);

					if (teamOwner != null) {

						removeTeamOwner(teamOwner.getId());

					}

				} catch (Exception exception) {

				}

				try {

					List<TeamJoinRequest> list = teamJoinRequestRepository.findByReceiverId(athelteId);

					if (list.isEmpty()) {

						throw new Exception();

					}

					for (TeamJoinRequest i : list) {

						if (i.getRoleType().equals(TeamJoinRequestRole.ROLE_ATHLETE)) {

							removeTeamJoinRequest(i.getId());

						}

					}

				} catch (Exception e) {

				}

				try {

					AthleteLocation athleteLocation = athleteLocationRepository.findByAthleteId(athelteId);

					if (athleteLocation != null) {

						removeAthleteLocation(athleteLocation.getId());

					}

				} catch (Exception e) {

				}

				Team team = teamRepository.findByAtheletesContainingIgnoreCase(athelteId);

				if (team != null) {

					team.getAtheletes().remove(athelteId);

					teamRepository.save(team);

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeCoach(String coachId) {

		try {

			Coach coach = coachRepository.findById(coachId).get();

			if (coach != null) {

				long count = coachRepository.count();

				coachRepository.deleteById(coachId);

				if (count != coachRepository.count()) {

					try {

						List<TeamJoinRequest> list = teamJoinRequestRepository.findByReceiverId(coachId);

						if (!list.isEmpty()) {

							for (TeamJoinRequest i : list) {

								if (i.getRoleType().equals(TeamJoinRequestRole.ROLE_COACH)) {

									removeTeamJoinRequest(i.getId());

								}

							}

						}

					} catch (Exception e) {

					}

					Team team = teamRepository.findByCoachesContainingIgnoreCase(coachId);

					if (team != null) {

						team.getCoaches().remove(coachId);

						teamRepository.save(team);

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeScout(String scoutId) {

		try {

			Scouts scout = scoutsRepository.findById(scoutId).get();

			if (scout != null) {

				long count = scoutsRepository.count();

				scoutsRepository.deleteById(scoutId);

				if (count != scoutsRepository.count()) {

					try {

						List<TeamJoinRequest> list = teamJoinRequestRepository.findByReceiverId(scoutId);

						if (!list.isEmpty()) {

							for (TeamJoinRequest i : list) {

								if (i.getRoleType().equals(TeamJoinRequestRole.ROLE_SCOUT)) {

									removeTeamJoinRequest(i.getId());

								}

							}

						}

					} catch (Exception e) {

					}

					Team team = teamRepository.findByScoutsContainingIgnoreCase(scoutId);

					if (team != null) {

						team.getScouts().remove(scoutId);

						teamRepository.save(team);

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeTeamOwner(String teamOwnerId) {

		try {

			TeamOwner teamOwner = teamOwnerRepository.findById(teamOwnerId).get();

			if (teamOwner != null) {

				long count = teamOwnerRepository.count();

				teamOwnerRepository.deleteById(teamOwnerId);

				if (count != teamOwnerRepository.count()) {

					List<String> list = teamOwner.getTeams();

					if (!list.isEmpty()) {

						for (String i : list) {

							removeTeam(i);

						}

					}

					list = new ArrayList<>();

					list = teamOwner.getMatches();

					if (!list.isEmpty()) {

						for (String i : list) {

							removeMatch(i);

						}

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeTeam(String teamId) {

		try {

			Team team = teamRepository.findById(teamId).get();

			if (team != null) {

				long count = teamRepository.count();

				teamRepository.deleteById(teamId);

				if (count != teamRepository.count()) {

					try {

						List<Athelete> list = atheleteRepository.findByPresentTeamIgnoreCase(team.getTeamName());

						if (!list.isEmpty()) {

							for (Athelete i : list) {

								i.setPresentTeam("");

								atheleteRepository.save(i);

							}

						}

					} catch (Exception exception) {

					}

					try {

						List<String> coaches = team.getCoaches();

						if (!coaches.isEmpty()) {

							for (String i : coaches) {

								try {

									Coach coach = coachRepository.findById(i).get();

									coach.setTeamName("");

									coachRepository.save(coach);

									try {

										Team teams = teamRepository
												.findByAtheletesContainingIgnoreCase(coach.getAtheleteId());

										if (teams == null) {

											Athelete athelete = atheleteRepository.findById(coach.getAtheleteId())
													.get();

											athelete.setPresentTeam("");

											atheleteRepository.save(athelete);

										}

									} catch (Exception e) {

										Athelete athelete = atheleteRepository.findById(coach.getAtheleteId()).get();

										athelete.setPresentTeam("");

										atheleteRepository.save(athelete);

									}

								} catch (Exception e) {

								}

							}

							try {

								List<Match> list = matchRepository.findByTeamsContainingIgnoreCase(teamId);

								if (!list.isEmpty()) {

									for (Match j : list) {

										removeMatch(j.getId());

									}

								}

							} catch (Exception e) {

							}

						}

					} catch (Exception e) {

					}

					try {

						try {

							List<String> list = team.getScouts();

							for (String i : list) {

								try {

									Scouts scouts = scoutsRepository.findById(i).get();

									try {

										Team teams = teamRepository
												.findByAtheletesContainingIgnoreCase(scouts.getAtheleteId());

										if (teams == null) {

											Athelete athelete = atheleteRepository.findById(scouts.getAtheleteId())
													.get();

											athelete.setPresentTeam("");

											atheleteRepository.save(athelete);

										}

									} catch (Exception e) {

										Athelete athelete = atheleteRepository.findById(scouts.getAtheleteId()).get();

										athelete.setPresentTeam("");

										atheleteRepository.save(athelete);

									}

								} catch (Exception e) {

								}

							}

						} catch (Exception e) {

						}

						try {

							for (String doctorId : team.getDoctors()) {

								try {

									removeDoctor(doctorId);

								} catch (Exception e) {

								}

							}

						} catch (Exception e) {

						}

						try {

							TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(teamId);

							if (teamOwner == null) {

								throw new Exception();

							}

							teamOwner.getTeams().remove(teamId);

							teamOwnerRepository.save(teamOwner);

						} catch (Exception e) {

						}

						try {

							List<Match> _list = matchRepository.findByTeamsContainingIgnoreCase(teamId);

							if (!_list.isEmpty()) {

								for (Match j : _list) {

									removeMatch(j.getId());

								}

							}

						} catch (Exception e) {

						}

					} catch (Exception e) {

					}
					
					try {
						
						TeamLocationModel  teamLocation = teamLocationRepository.findByTeamId(teamId);
						
						if(teamLocation == null) {
							
							throw new Exception();
							
						}
						
						removeTeamLocation(teamLocation.getId());
						
					} catch(Exception e) {
						
					}

					List<Match> matches = matchRepository.findByTeamsContainingIgnoreCase(team.getId());

					if (!matches.isEmpty()) {

						for (Match i : matches) {

							removeMatch(i.getId());

						}

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeMatch(String matchId) {

		try {

			System.out.println("match id :- " + matchId);

			Match match = matchRepository.findById(matchId).get();

			if (match != null) {

				long count = matchRepository.count();

				matchRepository.deleteById(matchId);

				if (count != matchRepository.count()) {

					try {
						
						MatchName matchName = matchNameRepository.findByMatchId(match.getId());
						
						if(matchName != null) {
							
							removeMatchName(matchName.getId());
							
						}
						
					} catch(Exception e) {
						
					}
					
					try {

						MatchVenue matchVenues = matchVenueRepository.findByMatchId(match.getId());

						if (matchVenues != null) {

							removeMatchVenue(matchVenues.getId());

						}

					} catch (Exception e) {

					}

					System.out.println("Match is deleted...");

					List<Athelete> list = atheleteRepository.findByEventAttendenceContainingIgnoreCase(matchId);

					if (!list.isEmpty()) {

						for (Athelete i : list) {

							while (i.getEventAttendence().contains(matchId)) {

								i.getEventAttendence().remove(matchId);

							}

							atheleteRepository.save(i);

						}

					}

					try {

						List<Scouts> _list = scoutsRepository.findByMatchesContainingIgnoreCase(matchId);

						if (!_list.isEmpty()) {

							for (Scouts scout : _list) {

								while (scout.getMatches().contains(matchId)) {

									scout.getMatches().remove(matchId);

								}

								scoutsRepository.save(scout);

							}

						}

					} catch (Exception e) {

					}

					try {

						List<Scouts> _list = scoutsRepository.findByEventsContainingIgnoreCase(matchId);

						if (!_list.isEmpty()) {

							for (Scouts scout : _list) {

								while (scout.getEvents().contains(matchId)) {

									scout.getEvents().remove(matchId);

								}

								scoutsRepository.save(scout);

							}

						}

					} catch (Exception e) {

					}

					List<String> teams = match.getTeams();

					for (String i : teams) {

						Team _team = null;

						try {

							_team = teamRepository.findById(i).get();

						} catch (Exception e) {

						}

						if (_team != null) {

							_team.getMatches().remove(matchId);

							teamRepository.save(_team);

							String teamId = _team.getId();

							try {

								TeamOwner teamOwner = teamOwnerRepository.findByTeamsContainingIgnoreCase(teamId);

								if (teamOwner != null) {

									System.out.println("teamOwner :- " + teamOwner.toString());

									if (teamOwner.getMatches().contains(match.getId())) {

										while (teamOwner.getMatches().contains(match.getId())) {

											teamOwner.getMatches().remove(match.getId());

										}

										teamOwnerRepository.save(teamOwner);

									}

									System.out.println("After refresh :- " + teamOwner.toString());

									try {

										Athelete _athelte = atheleteRepository.findById(teamOwner.getAtheleteId())
												.get();

										if (_athelte != null) {

											try {

												User user = userRepository.findById(_athelte.getUserId()).get();

												if (user != null) {

													EventOrganaizer eventOrganaizer = eventOrganaizerRepository
															.findByUserId(user.getId());

													if (eventOrganaizer != null) {

														while (eventOrganaizer.getMatches().contains(matchId)) {

															eventOrganaizer.getMatches().remove(matchId);

														}

														eventOrganaizerRepository.save(eventOrganaizer);

													}

												}
											} catch (Exception exception) {

											}

										}

									} catch (Exception exception) {

									}

								}

							} catch (Exception exception) {

								System.out.println("Exception in team owner :- " + exception.getMessage());

							}

						}

					}

				}

			}

		} catch (Exception e) {

			System.out.println(e.getMessage());

		}

	}

	public void removeEventOrganaizer(String eventOrganaizerId) {

		try {

			EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findById(eventOrganaizerId).get();

			if (eventOrganaizer != null) {

				long count = eventOrganaizerRepository.count();

				eventOrganaizerRepository.deleteById(eventOrganaizerId);

				if (count != eventOrganaizerRepository.count()) {

					List<String> matches = eventOrganaizer.getMatches();

					if (!matches.isEmpty()) {

						for (String i : matches) {

							removeMatch(i);

						}

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeBooking(String bookingId) {

		try {

			Booking booking = bookingRepository.findById(bookingId).get();

			if (booking == null) {

				throw new Exception();

			}

			long count = bookingRepository.count();

			bookingRepository.deleteById(booking.getId());

			if (count != bookingRepository.count()) {

				try {

					List<BkashTransaction> list = bkashTransactionRepository.findByBookingId(bookingId);

					for (BkashTransaction i : list) {

						removeBikashTransaction(i.getId());

					}

				} catch (Exception e) {

				}

				try {

					List<GroupBooking> groupBookings = groupBookingRepository.findByBookingId(bookingId);

					if (groupBookings.isEmpty()) {

						throw new Exception();

					}

					for (GroupBooking i : groupBookings) {

						removeGroupBooking(i.getId());

					}

				} catch (Exception e) {

				}

				EventOrganaizer eventOrganaizer = eventOrganaizerRepository.findByUserId(booking.getUserId());

				String venueId = booking.getVenueId();

				if (eventOrganaizer != null) {

					List<Match> list = matchRepository.findByOrganaizerId(eventOrganaizer.getId());

					for (Match i : list) {

						if (i.getMatchEndTime().isBefore(booking.getEndTime())
								&& i.getMatchStartTime().isAfter(booking.getStartTime())) {

							Instant now = Instant.now();

							if (now.isAfter(i.getMatchStartTime()) && now.isBefore(i.getMatchEndTime())) {

								try {

									MatchVenue matchVenue = matchVenueRepository.findByMatchId(i.getId());

									if (matchVenue != null && matchVenue.getVenueId().equals(venueId)) {

										removeMatch(i.getId());

									}

								} catch (Exception e) {

								}

							} else if (now.isBefore(i.getMatchStartTime())) {

								try {

									MatchVenue matchVenue = matchVenueRepository.findByMatchId(i.getId());

									if (matchVenue != null && matchVenue.getVenueId().equals(venueId)) {

										removeMatch(i.getId());

									}

								} catch (Exception e) {

								}

							}

						}

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeDiscount(String discountId) {

		try {

			Discount discount = discoutnRepository.findById(discountId).get();

			if (discount != null) {

				discoutnRepository.deleteById(discount.getId());

			}

		} catch (Exception e) {

		}

	}

	public void removeGroupBooking(String groupBookingId) {

		try {

			GroupBooking groupBooking = groupBookingRepository.findById(groupBookingId).get();

			if (groupBooking == null) {

				throw new Exception();

			}

			groupBookingRepository.deleteById(groupBookingId);

			removeBooking(groupBooking.getBookingId());

		} catch (Exception e) {

		}

	}

	public void removeVenueOwner(String venueOwnerId) {

		try {

			Owner owner = ownerRepository.findById(venueOwnerId).get();

			if (owner == null) {

				throw new Exception();

			}

			long count = ownerRepository.count();

			ownerRepository.deleteById(venueOwnerId);

			if (count != ownerRepository.count()) {

				List<Venue> list = venueReposiotry.findByOwnerId(owner.getUserId());

				if (!list.isEmpty()) {

					for (Venue venue : list) {

						removeVenue(venue.getId());

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeVenue(String venueId) {

		try {

			Venue venue = venueReposiotry.findById(venueId).get();

			if (venue != null) {

				long count = venueReposiotry.count();

				venueReposiotry.deleteById(venueId);

				if (count != venueReposiotry.count()) {

					try {

						List<MatchVenue> matchVenues = matchVenueRepository.findByVenueId(venue.getId());

						if (!matchVenues.isEmpty()) {

							for (MatchVenue i : matchVenues) {

								removeMatchVenue(i.getId());

							}

						}

						List<Booking> list = bookingRepository.findByVenueId(venueId);

						if (!list.isEmpty()) {

							for (Booking i : list) {

								removeBooking(i.getId());

								try {

									List<GroupBooking> groupBookings = groupBookingRepository
											.findByBookingId(i.getId());

									if (!groupBookings.isEmpty()) {

										for (GroupBooking j : groupBookings) {

											removeGroupBooking(j.getId());

										}

									}

								} catch (Exception exception) {

								}

							}

						}

					} catch (Exception e) {

					}

					VenueLocation venueLocation = venueLocationRepository.findByVenueId(venue.getId());

					if (venueLocation != null) {

						venueLocationRepository.deleteById(venueLocation.getId());

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeMatchVenue(String matchVenueId) {

		try {

			MatchVenue matchVenue = matchVenueRepository.findById(matchVenueId).get();

			if (matchVenue == null) {

				throw new Exception();

			}

			long count = matchVenueRepository.count();

			matchVenueRepository.deleteById(matchVenue.getId());

			if (count != matchVenueRepository.count()) {

				removeMatch(matchVenue.getMatchId());
				/*
				 * removeVenue(matchVenue.getVenueId());
				 * 
				 * VenueLocation venueLocation =
				 * venueLocationRepository.findByVenueId(matchVenue.getVenueId());
				 * 
				 * if (venueLocation != null) {
				 * 
				 * venueLocationRepository.deleteById(venueLocation.getId());
				 * 
				 * }
				 */

			}

		} catch (Exception e) {

		}

	}

	public void removeVenueLocation(String venueLocationId) {

		try {

			VenueLocation venueLocation = venueLocationRepository.findById(venueLocationId).get();

			if (venueLocation != null) {

				venueLocationRepository.deleteById(venueLocation.getId());

				removeVenue(venueLocation.getVenueId());

			}

		} catch (Exception e) {

		}

	}

	public void removeBikashTransaction(String bkashTransactionId) {

		try {

			BkashTransaction bkashTransaction = bkashTransactionRepository.findById(bkashTransactionId).get();

			if (bkashTransaction == null) {

				throw new Exception();

			}

			bkashTransactionRepository.deleteById(bkashTransactionId);

		} catch (Exception e) {

		}

	}

	public void removeNotification(String notificationId) {

		try {

			Notification notification = notificationRepository.findById(notificationId).get();

			if (notification == null) {

				throw new Exception();

			}

			notificationRepository.deleteById(notificationId);

		} catch (Exception e) {

		}

	}

	public void removeChatMessage(String chatMessageId) {

		try {

			ChatMessage chatMessage = chatMessageRepository.findById(chatMessageId).get();

			if (chatMessage == null) {

				throw new Exception();

			}

			chatMessageRepository.deleteById(chatMessageId);

		} catch (Exception e) {

		}

	}

	public void removeTeamJoinRequest(String teamJoinRequestId) {

		try {

			TeamJoinRequest teamJoinRequest = teamJoinRequestRepository.findById(teamJoinRequestId).get();

			if (teamJoinRequest == null) {

				throw new Exception();

			}

			teamJoinRequestRepository.deleteById(teamJoinRequest.getId());

		} catch (Exception e) {

		}

	}

	public void removeProfileImage(String profileImageId) {

		try {

			ProfileIamge profileImage = profileImageRepository.findById(profileImageId).get();

			if (profileImage != null) {

				long count = profileImageRepository.count();

				profileImageRepository.deleteById(profileImage.getId());

				if (count != profileImageRepository.count()) {

					imageService.delete(profileImage.getImageHex());

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeCV(String cvId) {

		try {

			CVUploadModel model = cvUploadRepository.findById(cvId).get();

			if (model != null) {

				long count = cvUploadRepository.count();

				cvUploadRepository.deleteById(cvId);

				if (count != cvUploadRepository.count()) {

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeDoctor(String doctorId) {

		try {

			Doctor doctor = doctorRepository.findById(doctorId).get();

			if (doctor != null) {

				long count = doctorRepository.count();

				doctorRepository.deleteById(doctorId);

				if (count != doctorRepository.count()) {

					List<TeamJoinRequest> list = teamJoinRequestRepository.findByReceiverId(doctor.getId());

					for (TeamJoinRequest i : list) {

						removeTeamJoinRequest(i.getId());

					}

				}

			}

		} catch (Exception e) {

		}

	}

	public void removeAthleteLocation(String athleteLocationId) {

		try {

			AthleteLocation athleteLocation = athleteLocationRepository.findById(athleteLocationId).get();

			if (athleteLocation != null) {

				long count = athleteLocationRepository.count();

				athleteLocationRepository.deleteById(athleteLocationId);

				if (count != athleteLocationRepository.count()) {

					removeAthelete(athleteLocation.getAthleteId());

				}

			}

		} catch (Exception e) {

		}

	}
	
	public void removeTeamLocation(String id) {
		
		try {
			
			TeamLocationModel teamLocation = teamLocationRepository.findById(id).get();
			
			if(teamLocation != null) {
				
				long count = teamLocationRepository.count();
				
				teamLocationRepository.deleteById(id);
				
				if(count != teamLocationRepository.count()) {
					
					removeTeam(teamLocation.getTeamId());
					
				}
				
			}
			
		} catch(Exception e) {
			
		}
		
	}
	
	public void removeMatchName(String matchNameId) {
		
		try {
			
			MatchName matchName = matchNameRepository.findById(matchNameId).get();
			
			if(matchName != null) {
				
				long count = matchNameRepository.count();
				
				matchNameRepository.deleteById(matchNameId);
				
				if(count != matchNameRepository.count()) {
					
					removeMatch(matchName.getMatchId());
					
				}
				
			}
			
		} catch(Exception e) {
			
		}
		
	}

	public void removeGym(String gymId) {
		
		try {
			
			Gyms gyms = gymsRepository.findById(gymId).get();
			
			if(gyms != null) {
				
				long count = gymsRepository.count();
				
				gymsRepository.deleteById(gymId);
				
				if(count != gymsRepository.count()) {
					
				}
				
			}
			
		} catch(Exception e) {
			
		}
		
	}
	
}
