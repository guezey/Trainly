package com.fliers.trainly.models.trips;

import com.fliers.trainly.models.users.Customer;

/**
 * Ticket Object 
 * @author Cengizhan Terzioğlu
 * @version 22.04.2021
*/
public class Ticket {
    
    // Properties
    private Seat seat;
    private Customer customer;
    private String comment;
    private int starRating;

    // Constructors

    /**
     * Create a ticket for a seat
     * @param seat seat sold for that ticket
     * @param customer customer who bought the ticket
     */
    public Ticket( Seat seat, Customer customer ) {
        this.seat = seat;
        this.customer = customer;
        setComment( "" );
        setStarRating(0);
    }

    // Methods
    /**
     * Getter method for a ticket's seat
     * @return seat sold for that ticket
     */
    public Seat getSeat() {
        return this.seat;
    }

    /**
     * Getter method for a ticket's customer
     * @return who bought the ticket
     */
    public Customer getCustomer() {
        return this.customer;
    }

    /**
     * Getter method for a comment
     * @return comment written to the company by the customer
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Set method for a comment
     * @param comment comment written to the company by the customer
     */
    public void setComment( String comment ) {
        this.comment = comment;
    }

    /**
     * Getter method for star rating
     * @return star rating given to the company by the customer
     */
    public int getStarRating() {
        return this.starRating;
    }

    /**
     * Set method for star rating
     * @param starRating star rating given to the company by the customer
     */
    public void setStarRating( int starRating ) {
        this.starRating = starRating;
    }
}