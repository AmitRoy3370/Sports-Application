package com.example.demo700.CyclicCleaner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo700.Models.User;
import com.example.demo700.Models.Athlete.Athelete;
import com.example.demo700.Models.Athlete.Coach;
import com.example.demo700.Models.Athlete.Scouts;
import com.example.demo700.Models.Athlete.Team;
import com.example.demo700.Models.Athlete.TeamOwner;
import com.example.demo700.Models.EventOrganaizer.EventOrganaizer;
import com.example.demo700.Models.EventOrganaizer.Match;
import com.example.demo700.Models.EventOrganaizer.MatchVenue;
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
import com.example.demo700.Repositories.Athelete.TeamOwnerRepository;
import com.example.demo700.Repositories.Athelete.TeamRepository;
import com.example.demo700.Repositories.EventOrganaizer.EventOrganaizerRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchRepository;
import com.example.demo700.Repositories.EventOrganaizer.MatchVenueRepository;
import com.example.demo700.Repositories.Turf.BookingRepository;
import com.example.demo700.Repositories.Turf.DiscountRepository;
import com.example.demo700.Repositories.Turf.GroupBookingRepository;
import com.example.demo700.Repositories.Turf.OwnerRepository;
import com.example.demo700.Repositories.Turf.VenueLocationServiceRepository;
import com.example.demo700.Repositories.Turf.VenueRepository;

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

	public void removeUser(String userId) {

		try {

			User user = userRepository.findById(userId).get();

			if (user == null) {

				throw new Exception();

			}

			long count = userRepository.count();

			if (count != userRepository.count()) {

				userRepository.deleteById(userId);

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

								Coach coach = coachRepository.findById(i).get();

								coach.setTeamName("");

								coachRepository.save(coach);

								Athelete athelete = atheleteRepository.findById(coach.getAtheleteId()).get();

								athelete.setPresentTeam("");

								atheleteRepository.save(athelete);

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

						}

					} catch (Exception e) {

					}

					try {

						List<String> list = team.getScouts();

						for (String i : list) {

							Scouts scouts = scoutsRepository.findById(i).get();

							Athelete athelete = atheleteRepository.findById(scouts.getAtheleteId()).get();

							athelete.setPresentTeam("");

							atheleteRepository.save(athelete);

							try {

								List<Match> _list = matchRepository.findByTeamsContainingIgnoreCase(teamId);

								if (!_list.isEmpty()) {

									for (Match j : _list) {

										removeMatch(j.getId());

									}

								}

							} catch (Exception e) {

							}

						}

					} catch (Exception e) {

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

				try {

					MatchVenue matchVenues = matchVenueRepository.findByMatchId(match.getId());

					if (matchVenues != null) {

						removeMatchVenue(matchVenues.getId());

					}

				} catch (Exception e) {

				}

				long count = matchRepository.count();

				matchRepository.deleteById(matchId);

				if (count != matchRepository.count()) {

					System.out.println("Match is deleted...");

					List<Athelete> list = atheleteRepository.findByEventAttendenceContainingIgnoreCase(matchId);

					if (!list.isEmpty()) {

						for (Athelete i : list) {

							i.getEventAttendence().remove(matchId);

							atheleteRepository.save(i);

						}

					}

					try {

						List<Scouts> _list = scoutsRepository.findByMatchesContainingIgnoreCase(matchId);

						if (!_list.isEmpty()) {

							for (Scouts scout : _list) {

								scout.getMatches().remove(matchId);

								scoutsRepository.save(scout);

							}

						}

					} catch (Exception e) {

					}

					try {

						List<Scouts> _list = scoutsRepository.findByEventsContainingIgnoreCase(matchId);

						if (!_list.isEmpty()) {

							for (Scouts scout : _list) {

								scout.getEvents().remove(matchId);

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

										teamOwner.getMatches().remove(match.getId());

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

														eventOrganaizer.getMatches().remove(matchId);

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

			groupBookingRepository.findById(groupBookingId);

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

				List<Venue> list = venueReposiotry.findByOwnerId(venueOwnerId);

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
				removeVenue(matchVenue.getVenueId());

				VenueLocation venueLocation = venueLocationRepository.findByVenueId(matchVenue.getVenueId());

				if (venueLocation != null) {

					venueLocationRepository.deleteById(venueLocation.getId());

				}

			}

		} catch (Exception e) {

		}

	}

}
