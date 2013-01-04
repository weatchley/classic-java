package gov.ymp.csi.review;


import java.sql.*;
import javax.naming.*;
import java.util.ArrayList;


public class DataEngine {

   //public static void main(String[] args)  {

           //String driver = "oracle.jdbc.driver.OracleDriver";
           //String url = "jdbc:oracle:thin:@ydoradev:1521:ydor";
           //String username = "dattam";
           //String password = "Puchke-sona";
           //showReviewerTable(driver, url, username, password);

   //}


   public static void deleteReviewRow(String driver,
                                      String url,
                                      String username,
                                      String password,
                                      String reviewername,
                                      String docid)


   {

            try  {
		   	 	          //Load database driver if it is not already loaded.
		   	 	            Class.forName(driver);

		   	 	           // DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

		   	 	          //Establish network connection to database.
		   	 	          Connection conn = DriverManager.getConnection(url, username, password);

		   	 	          //Create a statement for executing queries.
		   	 	          Statement statement = conn.createStatement();


		                  //int userid = Integer.parseInt(reviewerid);
		                  int docno =  Integer.parseInt(docid);

		   	 	          String query = "DELETE FROM review WHERE documentid = " + docid + " AND reviewerid= (SELECT id FROM reviewer WHERE name= '" + reviewername + "')";


		                  System.out.println(query);
		   	 	          //Send query to database and store results.
		   	 	          statement.executeUpdate(query);


		   	 	          conn.close();


		   	    }
		   	 	        catch (ClassNotFoundException cnfe)  {
		   	 	          System.err.println("Error loading driver: " + cnfe);

		   	 	        }
		   	 	        catch(SQLException sqle)  {
		   	 	          System.err.println("Error with SQL: " + sqle);

		   	 	        }

		   	    }





   public static void insertReviewRow(String driver,
                                      String url,
                                      String username,
                                      String password,
                                      String rname,
                                      String rstatus,
                                      String reviewerid,
                                      String docid)


   {


	 	   try  {
	 	          //Load database driver if it is not already loaded.
	 	            Class.forName(driver);

	 	           // DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

	 	          //Establish network connection to database.
	 	          Connection conn = DriverManager.getConnection(url, username, password);
	 	          //System.out.println("Employee Id     Employee Name\n" + "========================");

	 	          //Create a statement for executing queries.
	 	          Statement statement = conn.createStatement();

	 	          //System.out.println(rname);
	 	         // System.out.println(reviewerid);
	 	          //System.out.println(docid);


                  int userid = Integer.parseInt(reviewerid);
                  int docno =  Integer.parseInt(docid);

	 	          String query = "INSERT INTO review(reviewid, reviewname, reviewerid, status, documentid) VALUES (review_sequence.nextval,'"+ rname +"',"+ userid+",'"+ rstatus + "',"+ docno +")";

                  //String query = "INSERT INTO review(reviewid, reviewname, reviewerid, status, documentid) VALUES (15, 'test' , 16 ,  'test1' , 8 )";


                  System.out.println(query);
	 	          //Send query to database and store results.
	 	          statement.executeUpdate(query);


	 	          //Print results.



	 	          conn.close();


	 	        }
	 	        catch (ClassNotFoundException cnfe)  {
	 	          System.err.println("Error loading driver: " + cnfe);

	 	        }
	 	        catch(SQLException sqle)  {
	 	          System.err.println("Error with SQL: " + sqle);

	 	        }

	    }



   public static String getDocumentName(String driver,
                                           String url,
                                           String username,
                                           String password,
                                           String aDocid)
   {
	   String docname = "";
	   try  {
	          //Load database driver if it is not already loaded.
	            Class.forName(driver);

	           // DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

	          //Establish network connection to database.
	          Connection conn = DriverManager.getConnection(url, username, password);
	          //System.out.println("Employee Id     Employee Name\n" + "========================");

	          //Create a statement for executing queries.
	          Statement statement = conn.createStatement();

	          String query = "SELECT documents.docname FROM documents WHERE docid=" + aDocid +"";

	          //Send query to database and store results.
	          ResultSet resultSet = statement.executeQuery(query);


	          //Print results.
	          if (resultSet != null)
	          {

	             while(resultSet.next())  {

	              //str += resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4) + "\n";

	              docname = resultSet.getString(1);

	             }

	          }
	          else
	          {
	   		   System.out.println("RESULT SET NULL!!!");
	   	   }
	          conn.close();

	          return(docname);
	        }
	        catch (ClassNotFoundException cnfe)  {
	          System.err.println("Error loading driver: " + cnfe);
	          return(null);
	        }
	        catch(SQLException sqle)  {
	          System.err.println("Error with connection: " + sqle);
	          return(null);
	        }

   }




   public static String getUserName(String driver,
                                    String url,
                                    String uname,
                                    String password,
                                    String aUserid)
      {
   	   String username = "";
   	   try  {
   	          //Load database driver if it is not already loaded.
   	            Class.forName(driver);

   	           // DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

   	          //Establish network connection to database.
   	          Connection conn = DriverManager.getConnection(url, uname, password);
   	          //System.out.println("Employee Id     Employee Name\n" + "========================");

   	          //Create a statement for executing queries.
   	          Statement statement = conn.createStatement();

   	          String query = "SELECT reviewer.name FROM reviewer WHERE id=" + aUserid +"";

   	          //Send query to database and store results.
   	          ResultSet resultSet = statement.executeQuery(query);


   	          //Print results.
   	          if (resultSet != null)
   	          {

   	             while(resultSet.next())  {

   	              //str += resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4) + "\n";

   	              username = resultSet.getString(1);

   	             }

   	          }
   	          else
   	          {
   	   		   System.out.println("RESULT SET NULL!!!");
   	   	   }
   	          conn.close();

   	          return(username);
   	        }
   	        catch (ClassNotFoundException cnfe)  {
   	          System.err.println("Error loading driver: " + cnfe);
   	          return(null);
   	        }
   	        catch(SQLException sqle)  {
   	          System.err.println("Error with connection: " + sqle);
   	          return(null);
   	        }

      }




   public static ArrayList showReviewerTable(String driver,
                                        String url,
                                        String username,
                                        String password,
                                        String aDocid)    {

     ArrayList str = new ArrayList();
     try  {
       //Load database driver if it is not already loaded.
         Class.forName(driver);

        // DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

       //Establish network connection to database.
       Connection conn = DriverManager.getConnection(url, username, password);
       //System.out.println("Employee Id     Employee Name\n" + "========================");

       //Create a statement for executing queries.
       Statement statement = conn.createStatement();

       String query = "SELECT documents.docname, review.reviewname, review.status , reviewer.name FROM documents , review , reviewer  WHERE review.reviewerid = reviewer.id and review.documentid=" + aDocid +"and review.documentid=documents.docid";

       System.out.println(query);

       //Send query to database and store results.
       ResultSet resultSet = statement.executeQuery(query);


       //Print results.
       if (resultSet != null)
       {

          while(resultSet.next())  {

           //str += resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4) + "\n";

           str.add(resultSet.getString(1));
           str.add(resultSet.getString(2));
           str.add(resultSet.getString(3));
           str.add(resultSet.getString(4));
           System.out.println("docname= " + resultSet.getString(1) + " reviewname= " + resultSet.getString(2) + "  status= " + resultSet.getString(3) + "  reviewer= " + resultSet.getString(4) + "\n");


          }

       }
       else
       {
		   System.out.println("RESULT SET NULL!!!");
	   }
       conn.close();

       return(str);
     }
     catch (ClassNotFoundException cnfe)  {
       System.err.println("Error loading driver: Table Not Found" + cnfe);

       return(null);
     }
     catch(SQLException sqle)  {
       System.err.println("Error with connection: " + sqle);
       return(null);
     }

   }
 }



