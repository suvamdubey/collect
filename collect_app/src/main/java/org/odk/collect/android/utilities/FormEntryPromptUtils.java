/*
 * Copyright 2017 Nafundi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.odk.collect.android.utilities;

import android.content.Context;

import org.javarosa.core.model.data.DateData;
import org.javarosa.core.model.data.DateTimeData;
import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.form.api.FormEntryPrompt;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

public class FormEntryPromptUtils {

    private FormEntryPromptUtils() {
    }

    public static String getAnswerText(FormEntryPrompt fep, Context context) {
        IAnswerData data = fep.getAnswerValue();
        final String appearance = fep.getQuestion().getAppearanceAttr();

        if (data instanceof DateTimeData) {
            return DateTimeUtils.getDateTimeLabel((Date) data.getValue(),
                    DateTimeUtils.getDatePickerDetails(appearance), true, context);
        }

        if (data instanceof DateData) {
            return DateTimeUtils.getDateTimeLabel((Date) data.getValue(),
                    DateTimeUtils.getDatePickerDetails(appearance), false, context);
        }

        if (data != null && appearance != null && appearance.contains("thousands-sep")) {
            try {
                final BigDecimal answerAsDecimal = new BigDecimal(fep.getAnswerText());

                DecimalFormat df = new DecimalFormat();
                df.setGroupingSize(3);
                df.setGroupingUsed(true);
                df.setMaximumFractionDigits(Integer.MAX_VALUE);

                // Use . as decimal marker for consistency with DecimalWidget
                DecimalFormatSymbols customFormat = new DecimalFormatSymbols();
                customFormat.setDecimalSeparator('.');

                if (df.getDecimalFormatSymbols().getGroupingSeparator() == '.') {
                    customFormat.setGroupingSeparator(' ');
                }

                df.setDecimalFormatSymbols(customFormat);

                return df.format(answerAsDecimal);
            } catch (NumberFormatException e) {
                return fep.getAnswerText();
            }
        }

        return fep.getAnswerText();
    }

    public static String markQuestionIfIsRequired(String questionText, boolean isRequired) {
        if (isRequired) {
            if (questionText == null) {
                questionText = "";
            }
            questionText = "<span style=\"color:#F44336\">*</span> " + questionText;
        }

        return questionText;
    }
}
