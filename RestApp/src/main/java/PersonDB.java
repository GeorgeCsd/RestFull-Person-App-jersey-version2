import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
@Path("/person")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class PersonDB {

    public  ArrayList<Person> Persons = new ArrayList<Person>();
    @GET
    @Path("/getAll")
    public  List<Person>  getAllPersons() throws ClassNotFoundException {

        Person p = null;
        Statement stmt = null;
        Connection con = null;
        PreparedStatement pstmt;
        ResultSet res;
        int i=0;

        try {

            con = DbConnection.getConnection();

            stmt = con.createStatement();

            pstmt = con.prepareStatement(
                    "SELECT ID,Age,Name FROM persontbl");

            res=pstmt.executeQuery();

            while (res.next() == true) {
                //ResultSetMetaData md = res.getMetaData();
                //int columns = md.getColumnCount();
                Persons.add(new Person(res.getInt(1),res.getInt(2),res.getString(3)));

            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PersonDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return Persons;
    }

    @GET
    @Path("/{id}/get")
    public static Object getPerson(@PathParam("id")int id) throws ClassNotFoundException {
        Person p = null;
        Statement stmt = null;
        Connection con = null;
        PreparedStatement pstmt;
        ResultSet res;
        Response response = new Response();
        int i=0;
        try {

            con = DbConnection.getConnection();

            stmt = con.createStatement();

            pstmt = con.prepareStatement(
                    "SELECT ID,Age,Name FROM persontbl WHERE ID=" + id);

            res=pstmt.executeQuery();

            if (res.next() == true) {
                p=new Person();
                p.setId(res.getInt(1));
                p.setAge(res.getInt(2));
                p.setName(res.getString(3));


            } else {
                response.setStatus(false);
                response.setMessage("Person with id "+id+ " don't exists");
                return response;
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PersonDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return p;
    }

    @POST
    @Path("/add")
    public static Response addPerson(Person p) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        PreparedStatement pstmt;
        ResultSet res;
        Response response = new Response();
        try {

            con = DbConnection.getConnection();
            stmt = con.createStatement();
            pstmt = con.prepareStatement(
                    "SELECT ID FROM persontbl WHERE ID=" + p.getId());
            res=pstmt.executeQuery();
            if(res.next()==true){
                response.setStatus(false);
                response.setMessage("Person Already Exists");
            }
            else{
                pstmt = con.prepareStatement(
                        "INSERT INTO persontbl VALUES(?,?,?)");
                pstmt.setInt(1,p.getId());
                pstmt.setString(2,p.getName());
                pstmt.setInt(3,p.getAge());
                pstmt.executeUpdate();
                response.setStatus(true);
                response.setMessage("Person created successfully");
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PersonDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection

            closeDBConnection(stmt, con);
            return response;
        }
    }

    @GET
    @Path("/{id}/delete")
    public static Response deletePerson(@PathParam("id")int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        PreparedStatement pstmt;
        ResultSet res;
        Response response = new Response();
        try {

            con = DbConnection.getConnection();
            stmt = con.createStatement();
            pstmt = con.prepareStatement(
                    "SELECT ID FROM persontbl WHERE ID=" + id);
            res=pstmt.executeQuery();
            if(res.next()==false){
                response.setStatus(false);
                response.setMessage("Person Don't Exists");
            }
            else{
                pstmt = con.prepareStatement(
                        "DELETE FROM persontbl WHERE ID=" + id);

                pstmt.executeUpdate();
                response.setStatus(true);
                response.setMessage("Person was deleted successfully");
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PersonDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
            return response;
        }
    }
    @PUT
    @Path("/{id}/update")
    public static Response updatePerson(@PathParam("id")int id,Person p) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        PreparedStatement pstmt;
        ResultSet res;
        Response response = new Response();
        try {

            con = DbConnection.getConnection();
            stmt = con.createStatement();
            pstmt = con.prepareStatement(
                    "SELECT ID FROM persontbl WHERE ID=" + id);
            res=pstmt.executeQuery();
            if(res.next()==false){
                response.setStatus(false);
                response.setMessage("Person Don't Exists");
            }
            else{
                pstmt = con.prepareStatement(
                        "UPDATE persontbl SET Name=?,Age=? WHERE ID=" + id);
                pstmt.setString(1,p.getName());
                pstmt.setInt(2,p.getAge());
                pstmt.executeUpdate();
                response.setStatus(true);
                response.setMessage("Person was updated successfully");
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PersonDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
            return response;
        }
    }
    @GET
    @Path("/{id}/getDummy")
    public static Person getDummyPerson(@PathParam("id") int id) throws ClassNotFoundException{
        Person p = new Person();
        p.setAge(99);
        p.setName("Dummy");
        p.setId(id);
        return p;
    }

    private static void closeDBConnection(Statement stmt, Connection con) {
        // Close connection
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(PersonDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(PersonDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
