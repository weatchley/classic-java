package gov.ymp.csi.auth;

// Support classes
import java.io.*;
import java.util.*;
import java.sql.*;
import gov.ymp.csi.db.*;
import gov.ymp.csi.UNID.*;


/**
* RAccess is the base class for a collection of rights's in the CSI, used mostly by the Rights object.
*
* @author   Bill Atchley
*/
public class RAccess {
    private long item = 0;
    private long group = 0;
    private String type = null;
    private boolean negate = false;
    private String constraints = null;

    /**
    * creates a new empty RAccess object
    */
    public RAccess () {
        item = 0;
    }

    /**
    * creates a new RAccess object and set the properties
    */
    public RAccess (long myItem, long myGroup, String myType, boolean neg) {
        item = myItem;
        group = myGroup;
        type = myType;
        negate = neg;
    }

    /**
    * creates a new RAccess object and set the properties
    */
    public RAccess (long myItem, long myGroup, String myType, boolean neg, String myCon) {
        item = myItem;
        group = myGroup;
        type = myType;
        negate = neg;
        constraints = myCon;
    }

    /**
    * Retrieve the item id for the current RAccess item
    *
    * @return id     The item id for the current RAccess item
    */
    public long getItemID() {
        long id = item;
        return(id);
    }

    /**
    * Retrieve the group id for the current RAccess item
    *
    * @return id     The group id for the current RAccess item
    */
    public long getGroupID() {
        long id = group;
        return(id);
    }

    /**
    * Retrieve the access type for the current RAccess item
    *
    * @return myType     The type for the current RAccess item
    */
    public String getType() {
        String myType = type;
        return(myType);
    }

    /**
    * Retrieve the negation status on the access type for the current RAccess item
    *
    * @return myNegate     The negation for the current RAccess item
    */
    public boolean getNegation() {
        boolean myNegate = negate;
        return(myNegate);
    }

    /**
    * Retrieve the constraints for the current RAccess item
    *
    * @return myCon     The constraints for the current RAccess item
    */
    public String getConstraint() {
        String myCon = constraints;
        return(myCon);
    }

    /**
    * Set the item id for the current RAccess item
    *
    * @param id      The item id
    */
    public void setItemID(long id) {
        item = id;
    }

    /**
    * Set the group id for the current RAccess item
    *
    * @param id      The group id
    */
    public void setGroupID(long id) {
        group = id;
    }

    /**
    * Set the access type for the current RAccess item
    *
    * @param myType      The access type
    */
    public void setType(String myType) {
        type = myType;
    }

    /**
    * Set the negation on the access type for the current RAccess item
    *
    * @param neg      The negation value
    */
    public void setNegation(boolean neg) {
        negate = neg;
    }

    /**
    * Set the constraints for the current RAccess item
    *
    * @param myCon      The constraints to set
    */
    public void setConstraint(String myCon) {
        constraints = myCon;
    }

}
