package backend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents a UserAccounts file, in memory. It
 * inherits read and write methods from xstreambackend.Data and overrides
 * the encode and decode methods. It has methods to add or
 * delete a xstreambackend.User and add or remove credit from a users
 * account. The methods provide validation in which errors are
 * reported to caller and logged.
 */
public class Accounts extends Data {

    /**
     * a map with key, username, and value, xstreambackend.User, that represents
     * the UserAccounts file
     */
    private Map<String, User> users;
    /**
     * an iterator of the users map
     */
    private Iterator<Map.Entry<String, User>> userIterator;
    /**
     * an integer that represents the count
     */
    private boolean reset;

    /**
     * default constructor for Accounts class.
     * This constructor is mainly used for testing purposes.
     */
    public Accounts() {
        super("UserAccounts.txt");
        this.users = new HashMap<String, User>();
        this.userIterator = users.entrySet().iterator();
        this.reset = true;
    }

    /**
     * Constructor method for Accounts class. It calls the super class
     * Data's constructor and builds the user accounts representation in
     * memory.
     * @param readFilename a string representing the path of the existing UserAccounts
     * file
     * @param writeFilename a string representing the path to the new UserAccounts file
     * to be written
     */
    public Accounts(String readFilename, String writeFilename) {
        super(readFilename, writeFilename);
        this.users = new HashMap<String, User>();
        // build user accounts in memory
        readData();
        this.userIterator = users.entrySet().iterator();
        this.reset = true;
    }

    /**
     * This method is responsible for creating a new user in memory. It
     * validates name is not already in the system, type is a valid type,
     * and credit does not exceed maximum or minimum. Any errors are reported back to caller.
     * @param name a string that represents the name of xstreambackend.User
     * @param type a string that represents the type of user
     * @param credit a double that represents a xstreambackend.User's available credit.
     * @return 0 on success, 1 on failure, 2 on UserExistsError, 3 on on
     * UserTypeError, 4 on UserCreditError
     */
    public int newUser(String name, String type, double credit) {

        if (name.length() > 15)
            return 1;

        // check if name already exists
        if (getUser(name) != null)
            // TODO: implement NameExistsError
            return 2;
        // check if type is valid
        if (!(type.equals("AA") || type.equals("FS") || type.equals("BS") || type.equals("SB"))) {
            // TODO: implement InvalidTypeError
            return 3;
        }
        if (credit < 0 || credit > 999999999)
            // TODO: implement InvalidCreditError
            return 4;

        // create new user
        User tmp = new User(name, type, credit);
        try {

            // add user to users map with key name
            this.users.put(name, tmp);

        } catch (IllegalArgumentException e) {
            // TODO: implement IllegalArgumentException
            e.printStackTrace(System.err);
            return 1;
        }
        return 0;
    }

    /**
     * This method is responsible for validating and deleting a user account
     * from memory. It validates that name exists on the system and reports
     * errors to caller. If all business constraints are met it deletes the
     * user from memory.
     * @param name a string that represents the name of xstreambackend.User
     * @return 0 on success, 1 on failure, 2 on UserDoesNotExistError
     */
    public int deleteUser(String name) {
        try {
            // remove the user from users map
            User tmp = this.users.remove(name);

            // If name does not exist
            if (tmp == null)
                // TODO: implement NameDoesNotExistError
                return 2;

        } catch (UnsupportedOperationException e) {
            e.printStackTrace(System.err);
            return 1;
        } catch (ClassCastException e) {
            e.printStackTrace(System.err);
            return 1;
        } catch (NullPointerException e) {
            e.printStackTrace(System.err);
            return 1;
        }
        // successfully deleted user
        return 0;
    }

    /**
     * This method is responsible for retrieving a xstreambackend.User account from the map
     * of accounts in the system. If user does not exist Null is returned.
     * @param name a string that represents the name of xstreambackend.User
     * @return an objects that represents a xstreambackend.User account on success, null on failure
     */
    public User getUser(String name) {
        //  initialize a tmp xstreambackend.User
        User tmp = null;
        try {
            // search the users map by key, name
            tmp = this.users.get(name);

        } catch (ClassCastException e) {
            e.printStackTrace(System.err);
        } catch (NullPointerException e) {
            e.printStackTrace(System.err);
        }

        return tmp;
    }

    /**
     * This method is overrides xstreambackend.Data's decode method by parsing a line of
     * text from a file and using the values to create a user in memory. It
     * is also responsible for validating the parsed values to ensure
     * business constraints. An error generated by and invalid value
     * will return a fatal error to the calling function.
     * @param line a string that represents a line from a file
     * @return 0 on success, 1 on failure, 2 on fatal error
     */
    public int decode(String line) {
        // check length of line is 27
        if (line.length() == 27)
            //TODO: LineLengthError
            return 2;

        // check mandatory blank spaces are respected
        if (line.charAt(15) != ' ' || line.charAt(18) != ' ')
            // TODO: FieldSpacingError
            return 2;

        // extract name
        String name = line.substring(0,15).trim();
        // extract type
        String type = line.substring(16, 18).trim();
        // extract credit
        double credit = new Double(line.substring(19));

        // create a user with method newUser
        // if newUser exist with failure its exit status is reported
        // to calling function
        return newUser(name, type, credit);
    }

    /**
     * This method overrides the encode method in data. The method get the
     * next user from the userIterator and converts its data to a formatted
     * string. name is 15 char with trailing spaces, type is 2 char, credit
     * is 9 digits with or without decimal and leading 0's. All fields have
     * a blank space between them. The formatted line is returned to be
     * written to file. If end of data set is reached a EOF is sent
     * signaling calling function to finish write.
     */
    public String encode() {
        if (reset) {
            this.userIterator = users.entrySet().iterator();
            this.reset = false;
        }
        // create a tmp string
        String line = null;
        // if there is another record in memory
        if(this.userIterator.hasNext()) {
            // make a tmp copy of the record
            User cur = (this.userIterator.next()).getValue();
            if (cur.getCredit() == (int) cur.getCredit())
                line = String.format("%-15s %-2s %09d", cur.getName(), cur.getType(), (int) cur.getCredit());
            else
                // format the records attributes to file properties
                line = String.format("%-15s %-2s %06.2f", cur.getName(), cur.getType(), cur.getCredit());
        }
        return line;
    }

}