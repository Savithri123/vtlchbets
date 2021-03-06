package ua.kpi.leshchenko.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ua.kpi.leshchenko.beans.Bet;
import ua.kpi.leshchenko.connection.Database;

public class BetDAOImpl implements BetDAO {

	private static Logger logger = Logger.getLogger(BetDAOImpl.class.getName());
	private final String sqlCreate = "INSERT INTO mydb.bets(event,user,winner,betvalue) VALUES(?,?,?,?)";
	private final String sqlRead = "SELECT * FROM mydb.bets WHERE idbets = ";
	private final String sqlUpdate = "UPDATE mydb.bets SET event=?, user=?, winner=?, betvalue=? WHERE idbets=?";
	private final String sqlDelete = "DELETE FROM mydb.bets WHERE idbets=";
	private Database db;

	public BetDAOImpl(Database db) {
		this.db = db;
	}

	@Override
	public boolean create(Bet bet) {
		Connection conn = db.getConn();
		try (PreparedStatement ps = conn.prepareStatement(sqlCreate)) {
			ps.setInt(1, bet.getEvent());
			ps.setInt(2, bet.getUser());
			ps.setString(3, bet.getWinner());
			ps.setDouble(4, bet.getBetValue());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("DB problems create() ", e);
			return false;
		} finally {
			db.returnConnectionToPool(conn);
		}
		logger.info("New bet was created!" + bet);
		return true;
	}

	@Override
	public Bet read(int id) {
		Bet bet = new Bet();
		Connection conn = db.getConn();
		try (ResultSet rs = conn.createStatement().executeQuery(sqlRead + id)) {
			while (rs.next()) {
				bet.setIdBet(rs.getInt("idbets"));
				bet.setEvent(rs.getInt("event"));
				bet.setUser(rs.getInt("user"));
				bet.setWinner(rs.getString("winner"));
				bet.setBetValue(rs.getDouble("betvalue"));
			}
		} catch (SQLException e) {
			logger.error("DB problems read() ", e);
			return null;
		} finally {
			db.returnConnectionToPool(conn);
		}
		logger.info("Read bet id = " + id);
		return bet;
	}

	@Override
	public boolean update(Bet bet) {
		Connection conn = db.getConn();
		try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
			ps.setInt(1, bet.getEvent());
			ps.setInt(2, bet.getUser());
			ps.setString(3, bet.getWinner());
			ps.setDouble(4, bet.getBetValue());
			ps.setInt(5, bet.getIdBet());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.error("DB problem update() ", e);
			return false;
		} finally {
			db.returnConnectionToPool(conn);
		}
		logger.info("Bet " + bet.getIdBet() + " was updated!");
		return true;
	}

	@Override
	public boolean delete(int id) {
		Connection conn = db.getConn();
		try (Statement stmn = conn.createStatement()) {
			stmn.executeUpdate(sqlDelete + id);
		} catch (SQLException e) {
			logger.error("DB problems delete() ", e);
			return false;
		} finally {
			db.returnConnectionToPool(conn);
		}
		logger.info("Bet " + id + " was deleted!");
		return true;
	}

	@Override
	public ArrayList<Bet> findByUserFinished(int id) {
		ArrayList<Bet> betList = new ArrayList<>();
		Connection conn = db.getConn();
		try (ResultSet rs = conn.createStatement().executeQuery(
				"SELECT idbets,betvalue,winner,team1,team2,result FROM mydb.bets JOIN mydb.event ON mydb.bets.event = mydb.event.idevent WHERE user="
						+ id + " AND result IS NOT NULL")) {
			while (rs.next()) {
				Bet bet = new Bet();
				bet.setIdBet(rs.getInt("idbets"));
				bet.setBetValue(rs.getDouble("betvalue"));
				bet.setWinner(rs.getString("winner"));
				bet.setTeam1(rs.getString("team1"));
				bet.setTeam2(rs.getString("team2"));
				bet.setResult(rs.getString("result"));
				betList.add(bet);
			}
		} catch (Exception e) {
			logger.error("BetDAO.findByUser() problems.");
			return null;
		} finally {
			db.returnConnectionToPool(conn);
		}
		logger.info("BetDAO.findByUser() is ok.");
		return betList;
	}

	@Override
	public ArrayList<Bet> findByUserUpcoming(int id) {
		ArrayList<Bet> betList = new ArrayList<>();
		Connection conn = db.getConn();
		try (ResultSet rs = conn.createStatement().executeQuery(
				"SELECT idbets,betvalue,winner,team1,team2,result FROM mydb.bets JOIN mydb.event ON mydb.bets.event = mydb.event.idevent WHERE user="
						+ id + " AND result IS NULL")) {
			while (rs.next()) {
				Bet bet = new Bet();
				bet.setIdBet(rs.getInt("idbets"));
				bet.setBetValue(rs.getDouble("betvalue"));
				bet.setWinner(rs.getString("winner"));
				bet.setTeam1(rs.getString("team1"));
				bet.setTeam2(rs.getString("team2"));
				bet.setResult(rs.getString("result"));
				betList.add(bet);
			}
		} catch (Exception e) {
			logger.error("BetDAO.findByUser() problems.");
			return null;
		} finally {
			db.returnConnectionToPool(conn);
		}
		logger.info("BetDAO.findByUser() is ok.");
		return betList;
	}

	@Override
	public ArrayList<Bet> findAll() {
		ArrayList<Bet> betList = new ArrayList<>();
		Connection conn = db.getConn();
		try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM mydb.bets")) {
			while (rs.next()) {
				Bet bet = new Bet();
				bet.setIdBet(rs.getInt("idbets"));
				bet.setEvent(rs.getInt("event"));
				bet.setUser(rs.getInt("user"));
				bet.setWinner(rs.getString("winner"));
				bet.setBetValue(rs.getDouble("betvalue"));
				betList.add(bet);
			}
		} catch (Exception e) {
			logger.error("BetDAO.findAll() problems.");
			return null;
		} finally {
			db.returnConnectionToPool(conn);
		}
		logger.info("BetDAO.findAll() is ok.");
		return betList;
	}

	@Override
	public ArrayList<Bet> findByEvent(int id) {
		ArrayList<Bet> betList = new ArrayList<>();
		Connection conn = db.getConn();
		try (ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM mydb.bets WHERE event=" + id)) {
			while (rs.next()) {
				Bet bet = new Bet();
				bet.setIdBet(rs.getInt("idbets"));
				bet.setEvent(rs.getInt("event"));
				bet.setUser(rs.getInt("user"));
				bet.setWinner(rs.getString("winner"));
				bet.setBetValue(rs.getDouble("betvalue"));
				betList.add(bet);
			}
		} catch (Exception e) {
			logger.error("BetDAO.findByEvent() problems.");
			return null;
		} finally {
			db.returnConnectionToPool(conn);
		}
		logger.info("BetDAO.findByEvent() is ok.");
		return betList;

	}

}
