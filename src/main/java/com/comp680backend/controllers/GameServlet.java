package com.comp680backend.controllers;

import com.comp680backend.models.Game;
import com.comp680backend.util.CloudSqlManager;
import com.google.gson.Gson;
import org.apache.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GameServlet", value = "/games")
public class GameServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection conn = CloudSqlManager.getInstance().getConn();
        String getAllGames = "SELECT * FROM games";
        List<Game> games = new ArrayList<>();
        try {
            ResultSet resultSet = conn.prepareStatement(getAllGames).executeQuery();
            while (resultSet.next()) {
                Game game = new Game();
                game.setId(resultSet.getInt("id"));
                game.setName(resultSet.getString("name"));
                game.setPublisher(resultSet.getString("publisher"));
                game.setReleaseDate(resultSet.getTimestamp("release_date"));
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new Gson().toJson(games, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        System.out.println("POST NAME " + name);
        Long releaseDate = Long.valueOf(req.getParameter("releaseDate"));
        String publisher = req.getParameter("publisher");

        String insertGame = "INSERT INTO games (name, release_date, publisher) VALUES (?,?,?);";
        Connection conn = CloudSqlManager.getInstance().getConn();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(insertGame);
            preparedStatement.setString(1, name);
            preparedStatement.setTimestamp(2, new Timestamp(releaseDate));
            preparedStatement.setString(3, publisher);
            preparedStatement.execute();
            resp.setStatus(HttpStatus.SC_OK);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpStatus.SC_BAD_REQUEST);
        }
    }
}
