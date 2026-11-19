package team.projectpulse.evaluation;

public class RatingAverage {

    private Integer criterionId;
    private Double averageScore;

    public RatingAverage() {
        // Jackson needs a no-arg constructor to deserialize this type; the other constructors would hide the default one.
    }

    public RatingAverage(Integer criterionId, Double averageScore) {
        this.criterionId = criterionId;
        this.averageScore = averageScore;
    }

    public Integer getCriterionId() {
        return criterionId;
    }

    public void setCriterionId(Integer criterionId) {
        this.criterionId = criterionId;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

}
