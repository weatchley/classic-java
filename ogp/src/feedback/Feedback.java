
package feedback;
import java.net.*;
import java.io.OutputStreamWriter;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;

/**
  * This servlet is associated with the feedback form on the OCRWM Gateway.
  * It emails submissions to the feedback form to the intranetwebmaster@ymp.gov account.
  * Emails submitted to this account are available through the Lotus Notes email database established
  * for this user.
  */

public class Feedback extends HttpServlet {
  public void init()
    throws ServletException
  {

  }
	public void doPost(HttpServletRequest request,
						HttpServletResponse response)
		throws ServletException, IOException
	  {
		PrintWriter out = response.getWriter();

		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String comments = request.getParameter("comments");
		String ipaddress = request.getParameter("ipaddress");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.ymp.gov");
		props.put("mail.debug", "true");
		Session session = Session.getInstance(props, null);

		try {

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(email));
			InternetAddress[] address = {new InternetAddress("OCRWM_Gateway_Administrators@ymp.gov")};
				//InternetAddress[] address = {new InternetAddress("shuhei.higashi@ymp.gov")};
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("OCRWM Gateway Comments");
			msg.setSentDate(new Date());
			// If the desired charset is known, you can use
			// setText(text, charset)
			msg.setText("Name: " + name + "\nPhone: " + phone + "\nEmail: " + email + "\nComments: " + comments + "\nIP Address: " + ipaddress);
			Transport.send(msg);
			response.setContentType("text/html");
			out.println("<html>");
			out.println("<head>");
			out.println("<script language=javascript>");
			out.println("<!--\n");
			//out.println("window.close();\n");
			out.println("location.href='fform.htm';");
			out.println("//-->");
			out.println("</script>");
			out.println("</head>");
			out.println("<body>");
			out.println("</body>");
			out.println("</html>");
		}
		catch (MessagingException mex)
		{
			out.println("\n--Exception handling in msgsendsample.java");
			getServletContext().log(mex,mex.getMessage());

		   // mex.printStackTrace();
			out.println();
			Exception ex = mex;
			do {
			if (ex instanceof SendFailedException) {
				SendFailedException sfex = (SendFailedException)ex;
				Address[] invalid = sfex.getInvalidAddresses();
				if (invalid != null) {
				out.println("    ** Invalid Addresses");
				if (invalid != null) {
					for (int i = 0; i < invalid.length; i++)
					System.out.println("         " + invalid[i]);
				}
				}
				Address[] validUnsent = sfex.getValidUnsentAddresses();
				if (validUnsent != null) {
				out.println("    ** ValidUnsent Addresses");
				if (validUnsent != null) {
					for (int i = 0; i < validUnsent.length; i++)
					out.println("         "+validUnsent[i]);
				}
				}
				Address[] validSent = sfex.getValidSentAddresses();
				if (validSent != null) {
				out.println("    ** ValidSent Addresses");
				if (validSent != null) {
					for (int i = 0; i < validSent.length; i++)
					out.println("         "+validSent[i]);
				}
				}
			}
			out.println();
			if (ex instanceof MessagingException)
				ex = ((MessagingException)ex).getNextException();
			else
				ex = null;
			} while (ex != null);
		}
	  out.close();
	}
	public void destroy()
	{
		// nothing to do
	}
}


