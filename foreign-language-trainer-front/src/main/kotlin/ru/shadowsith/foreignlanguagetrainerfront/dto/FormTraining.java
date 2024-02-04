package ru.shadowsith.foreignlanguagetrainerfront.dto;

import java.util.ArrayList;
import java.util.List;

public class FormTraining {
    private List<Answer> translations;

    public FormTraining() {
        this.translations = new ArrayList<>();
    }

    public FormTraining(List<Answer> translations) {
        this.translations = translations;
    }

    public List<Answer> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Answer> translations) {
        this.translations = translations;
    }

    public void addTranslation(Answer translation) {
        this.translations.add(translation);
    }
}
