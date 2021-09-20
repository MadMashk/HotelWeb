package hibernate.sortings;

public enum RentSort  implements ISortType{

    ASC_SORT_BY_DEPARTURE_DATE( 1),
    DESC_SORT_BY_DEPARTURE_DATE(2);


    private final Integer sortIndex;

    RentSort( Integer sortIndex){

        this.sortIndex= sortIndex;
    }


    public Integer getSortIndex() {
        return sortIndex;
    }
}
