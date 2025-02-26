package cn.newness.imip.framework.jackson.core.databind;

import cn.newness.imip.framework.common.util.date.DateUtils;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**添加一个按照指定时间格式的转换器
 * @author machuran
 * @date 9/8/2024
 * @time 9:40 AM
 * @Description
 */
public class TimeFormatLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException{
        return LocalDateTime.parse(jsonParser.getText(), DateTimeFormatter.ofPattern(DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
    }
}
