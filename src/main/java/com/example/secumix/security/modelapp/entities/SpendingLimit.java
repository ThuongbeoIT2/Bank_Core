package com.example.secumix.security.modelapp.entities;

import com.example.secumix.security.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "spendingLimit")
@Table(name = "spendingLimit")
public class SpendingLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int spendId;
    @Column(name = "spendingLimit")
    @Min(value = 0,message = "Hạn mức tiêu dùng là số dương")
    private long spendingLimit;
    @Min(value = 0,message = "Hạn mức tiêu dùng là số dương")
    private long expenditure;
    @Column(nullable = false)
    private String date;
    @ManyToOne
    @JoinColumn(name = "cateid",foreignKey = @ForeignKey(name = "fk_spend_category"))
    private Category category;
    @ManyToOne
    @JoinColumn(name = "userid",foreignKey = @ForeignKey(name = "fk_user_spend"))
    private User user;
    public static String processDate(String inputDate) {
        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfOutput = new SimpleDateFormat("MM/yyyy");
        sdfInput.setLenient(false);

        try {
            Date date = sdfInput.parse(inputDate);
            return sdfOutput.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return sdfOutput.format(new Date());
        }
    }
}
