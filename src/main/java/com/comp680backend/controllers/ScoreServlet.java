package com.comp680backend.controllers;

import com.comp680backend.models.Score;
import com.comp680backend.util.CloudSqlManager;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ScoreServlet", value = "/score")
public class ScoreServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer gameId = Integer.valueOf(req.getParameter("gameId"));
        String getScoresList = "SELECT * FROM scores WHERE games_id = ? ORDER BY score DESC";

        Connection conn = CloudSqlManager.getInstance().getConn();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(getScoresList);
            preparedStatement.setInt(1, gameId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Score> scores = new ArrayList<>();
            while(resultSet.next()) {
                Score score = new Score();
                score.setId(resultSet.getInt("id"));
                score.setScore(resultSet.getInt("score"));
                score.setImageUrl(resultSet.getString("image_url"));
                score.setGameId(resultSet.getInt("games_id"));
                score.setUserId(resultSet.getInt("users_id"));
                scores.add(score);
            }
            resp.setStatus(HttpStatus.SC_OK);
            new Gson().toJson(scores, resp.getWriter());
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer gameId = Integer.valueOf(req.getParameter("gameId"));
        Integer userId = Integer.valueOf(req.getParameter("userID"));
        Integer score = Integer.valueOf(req.getParameter("score"));
        String imageUrl = req.getParameter("imageUrl");

        String insertNewScore = "INSERT INTO scores (gameId, userId, score, image_url) VALUES (?,?,?,?)";

        Connection conn = CloudSqlManager.getInstance().getConn();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertNewScore);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, score);
            preparedStatement.setString(4, imageUrl);
            preparedStatement.execute();
            resp.setStatus(HttpStatus.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
    }
}
