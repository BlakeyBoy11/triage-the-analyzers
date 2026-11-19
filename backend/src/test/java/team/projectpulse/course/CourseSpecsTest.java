package team.projectpulse.course;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * The search specs lower-case the column in SQL (LOWER, which does not depend on the JVM) and the search term in
 * Java. If Java lower-cases with the JVM's default locale, the two disagree on a server whose default locale is
 * Turkish, where "I".toLowerCase() is the dotless "ı": a search for "SENIOR" becomes "senıor" and matches nothing.
 */
@ExtendWith(MockitoExtension.class)
class CourseSpecsTest {

    @Mock
    Root<Course> root;
    @Mock
    CriteriaQuery<?> query;
    @Mock
    CriteriaBuilder criteriaBuilder;
    @Mock
    Path<String> column;
    @Mock
    Expression<String> loweredColumn;

    private Locale defaultLocale;

    @BeforeEach
    void runInTurkishLocale() {
        this.defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.forLanguageTag("tr-TR"));
    }

    @AfterEach
    void restoreLocale() {
        Locale.setDefault(this.defaultLocale);
    }

    @Test
    void containsCourseNameLowerCasesTheTermTheSameWayInEveryLocale() {
        given(this.root.<String>get("courseName")).willReturn(this.column);
        given(this.criteriaBuilder.lower(this.column)).willReturn(this.loweredColumn);

        CourseSpecs.containsCourseName("SENIOR DESIGN").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).like(this.loweredColumn, "%senior design%");
    }

    @Test
    void containsCourseDescriptionLowerCasesTheTermTheSameWayInEveryLocale() {
        given(this.root.<String>get("courseDescription")).willReturn(this.column);
        given(this.criteriaBuilder.lower(this.column)).willReturn(this.loweredColumn);

        CourseSpecs.containsCourseDescription("CAPSTONE DESIGN").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).like(this.loweredColumn, "%capstone design%");
    }

}
