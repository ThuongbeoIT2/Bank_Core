package com.example.secumix.security.modelapp.entities;



import com.example.secumix.security.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "trade")
@Table(name = "trade")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tradeId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String date;
    private Date createdAt;
    @Column(nullable = false)
    @Min(value = 0,message = "Giao dịch phải có giá trị")
    private long cost;
    @OneToOne
    @JoinColumn(name = "cateid",foreignKey = @ForeignKey(name = "fk_trade_cate"))
    private Category category;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "userId",foreignKey = @ForeignKey(name = "fk_user_trade"))
    private User user;
    public static String processDate(String inputDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            Date date = sdf.parse(inputDate);
            Date currentDate = new Date();
            if (date.compareTo(currentDate) <= 0) {
                return sdf.format(date);
            } else {
                return sdf.format(currentDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return sdf.format(new Date());
        }
    }
}
