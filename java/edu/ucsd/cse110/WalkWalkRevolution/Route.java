package edu.ucsd.cse110.WalkWalkRevolution;

/**
 *  Route object in charge of keeping track of arbitrary amount of route features, as well
 *  as a WalkData (which has nested Time data)
 *
 *  All fields of the Route object are currently allowed to be null, save for name, since all
 *    fields are optional
 */
public class Route implements Comparable<Route>{

    // Only required field
    public String name;

    // Can be null for uninitialized WalkData
    public WalkData walkdata;

    // Optional fields
    public String startLocation;


    public Boolean isFavorite = false;

    // 0 == none selected, 1 == first selected, 2 == second selected
    public int isFlat = 0;
    public int isDifficult = 0;
    public int isEven = 0;
    public int isTrail = 0;
    public int isLoop = 0;

    public String notes;

    // Since we have default member fields and null is allowed, empty ctor
    public Route(){ }

    // The FULL ctor, which takes care of every field Route object would need (0 subs fro null)
    public Route(String name, WalkData walkdata, String startLocation,
                    Boolean isFavorite, int isFlat, int isDifficult, int isEven,
                    int isTrail, int isLoop, String notes){
        this.name = name;
        this.walkdata = walkdata;

        this.startLocation = startLocation;

        // Optional Switches
        this.isFavorite = isFavorite;
        this.isFlat = isFlat;
        this.isDifficult = isDifficult;
        this.isEven = isEven;
        this.isTrail = isTrail;
        this.isLoop = isLoop;

        // Notes field
        this.notes = notes;
    }
    public Route setData(WalkData data){
        this.walkdata = data;
        return this;
    }
    // Name getters() and setters()
    public void setName(String name){
        this.name = name;
    }
    public String getName() { return this.name; }

    // sets the UI features save for the WalkData, which is populated seperately from the back end
    public void setUIFeatures(String name, String startLocation,
                            Boolean isFavorite, int isFlat, int isDifficult, int isEven,
                            int isTrail, int isLoop, String notes){
        this.name = name;

        this.startLocation = startLocation;

        // Optional Switches
        this.isFavorite = isFavorite;
        this.isFlat = isFlat;
        this.isDifficult = isDifficult;
        this.isEven = isEven;
        this.isTrail = isTrail;
        this.isLoop = isLoop;

        // Notes field
        this.notes = notes;
    }

    // Implements comparison feature to keep the RoutesList sorted, sorts (alphabetically) by name
    @Override
    public int compareTo(Route otherRoute) {
        return this.name.compareTo(otherRoute.name);
    }
}
