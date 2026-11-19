package team.projectpulse.instructor;

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
 * The search specs compare LOWER(column) in SQL with the search term lower-cased in Java. With the JVM's default
 * locale set to Turkish, "I".toLowerCase() is the dotless "ı", so "TIM" becomes "tım" and an exact match on
 * first name, last name, or email finds nothing.
 */
@ExtendWith(MockitoExtension.class)
class InstructorSpecsTest {

    @Mock
    Root<Instructor> root;
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
    void hasFirstNameLowerCasesTheTermTheSameWayInEveryLocale() {
        given(this.root.<String>get("firstName")).willReturn(this.column);
        given(this.criteriaBuilder.lower(this.column)).willReturn(this.loweredColumn);

        InstructorSpecs.hasFirstName("TIM").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).equal(this.loweredColumn, "tim");
    }

    @Test
    void hasLastNameLowerCasesTheTermTheSameWayInEveryLocale() {
        given(this.root.<String>get("lastName")).willReturn(this.column);
        given(this.criteriaBuilder.lower(this.column)).willReturn(this.loweredColumn);

        InstructorSpecs.hasLastName("SMITH").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).equal(this.loweredColumn, "smith");
    }

    @Test
    void hasEmailLowerCasesTheTermTheSameWayInEveryLocale() {
        given(this.root.<String>get("email")).willReturn(this.column);
        given(this.criteriaBuilder.lower(this.column)).willReturn(this.loweredColumn);

        InstructorSpecs.hasEmail("TIM.INSTRUCTOR@TCU.EDU").toPredicate(this.root, this.query, this.criteriaBuilder);

        verify(this.criteriaBuilder).equal(this.loweredColumn, "tim.instructor@tcu.edu");
    }

}
