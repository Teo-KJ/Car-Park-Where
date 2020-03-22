package com.example.carparkwhere.DAO.DAOInterfaces;
import com.example.carparkwhere.ModelObjects.Review;
import com.example.carparkwhere.FilesIdkWhereToPutYet.NetworkCallEventListener;


public interface ReviewDao {

    public void getCarparkReviewsByCarparkID(String carparkID, final NetworkCallEventListener networkCallEventListener);
    public void getCarparkReviewsByUserEmail(String userEmail, final NetworkCallEventListener networkCallEventListener);
    public void saveNewCarparkReview(Review review, final NetworkCallEventListener networkCallEventListener);
    public void updateCarparkReviewWithNewValues(String oldReviewID ,Review newReview, final NetworkCallEventListener networkCallEventListener);
    public void deleteCarparkReviewByReviewID(String reviewID, final NetworkCallEventListener networkCallEventListener);
    public void getCarparkAverageRating(String carparkID, final NetworkCallEventListener networkCallEventListener);
    public void getCarparkReviewsCount(String carparkID, final NetworkCallEventListener networkCallEventListener);

}
