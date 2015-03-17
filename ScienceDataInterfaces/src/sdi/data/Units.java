package sdi.data;

/**
 * representation of data units, which should follow the conventions:<ul>
 * <li>SI conventions should be used: m kg s A K mol cd
 * <li>asterisk should be used to indicate multiplication, carot for exponents.
 * <li>Exponents should be negative, rather than using solidus.  cm^-2
 * <li>Times should be represented as UNIT since ISO8601, such as seconds since 2000-01-01.
 * <li>arbitrary units are allowed, such as "apples"
 * </ul>
 * @see http://en.wikipedia.org/wiki/SI_derived_unit
 * 
 * @author faden@cottagesystems.com
 */
public class Units {

    private final String name;

    public Units(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Units [name=" + name + "]";
    }
}
