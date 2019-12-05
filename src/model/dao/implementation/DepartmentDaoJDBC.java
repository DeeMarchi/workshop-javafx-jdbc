package model.dao.implementation;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    private Department instantiateDepartment(ResultSet set) throws SQLException {
        Department department = new Department();
        department.setId(set.getInt("Id"));
        department.setName(set.getString("Name"));
        return department;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement(
                    "INSERT INTO department "
                            + "(Name) "
                            + "VALUES "
                            + "(?)",
                    statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, obj.getName());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet set = statement.getGeneratedKeys();
                if (set.next()) {
                    int id = set.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(set);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement(
                    "UPDATE department "
                            + "SET name = ? "
                            + "WHERE id = ?"
            );
            statement.setString(1, obj.getName());
            statement.setInt(2, obj.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement(
                    "DELETE FROM department where id = ?"
            );
            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet set = null;

        try {
            statement = conn.prepareStatement("SELECT * FROM department WHERE department.Id = ?");
            statement.setInt(1, id);
            set = statement.executeQuery();

            if (set.next()) {
                return instantiateDepartment(set);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(set);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement statement = null;
        ResultSet set= null;

        try {
            statement = conn.prepareStatement(
                    "SELECT * FROM department "
                        + "ORDER BY Name"
            );
            set = statement.executeQuery();

            List<Department> list = new ArrayList<>();
            while (set.next()) {
                list.add(instantiateDepartment(set));
            }
            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(set);
        }
    }
}
