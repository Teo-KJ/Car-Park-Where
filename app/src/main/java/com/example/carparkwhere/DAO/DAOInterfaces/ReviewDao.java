package com.example.carparkwhere.DAO.DAOInterfaces;
import com.example.carparkwhere.Entities.Review;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;


/**
 * This is the data access object class for accessing information related to reviews
 * @author kohsweesen
 */
public interface ReviewDao {

    /**
     * This function returns all the carpark reviews of a specified carpark
     * @param carparkID the carpark id of the specified carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getCarparkReviewsByCarparkID(String carparkID, final NetworkCallEventListener networkCallEventListener);


    /**
     * This function returns all the carpark reviews made by a specified user
     * @param userEmail the email address of the specified user
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getCarparkReviewsByUserEmail(String userEmail, final NetworkCallEventListener networkCallEventListener);


    /**
     * This function helps to save a new review into the database
     * @param review This is the review object to be saved into the database
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void saveNewCarparkReview(Review review, final NetworkCallEventListener networkCallEventListener);

    /**
     * This function helps to update a specified existing review with new values, such as updating comment or updating with new rating
     * @param oldReviewID this is the review id of the review before modification
     * @param newReview this is the new review object to be saved into the database
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void updateCarparkReviewWithNewValues(String oldReviewID ,Review newReview, final NetworkCallEventListener networkCallEventListener);


    /**
     * This function deletes a specified carpark review by specifying the review id.
     * @param reviewID The review id of the review to be deleted
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void deleteCarparkReviewByReviewID(String reviewID, final NetworkCallEventListener networkCallEventListener);

    /**
     * This function returns the average rating of the carpark.
     * @param carparkID The carpark id of the specified carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getCarparkAverageRating(String carparkID, final NetworkCallEventListener networkCallEventListener);

    /**
     * This function returns the number of reviews of a specified carpark
     * @param carparkID the carpark id of the specified carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getCarparkReviewsCount(String carparkID, final NetworkCallEventListener networkCallEventListener);

}
