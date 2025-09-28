package com.onidza.telegrambot.strategy;

import com.onidza.telegrambot.dto.SalaryDTO;
import com.onidza.telegrambot.dto.VacancyDTO;
import com.onidza.telegrambot.service.ProviderStrategy;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractStrategy implements ProviderStrategy {
    private static final String RUB_SYMBOL = "₽";

    abstract List<VacancyDTO> getVacancyList(String searchString);
    abstract Document getDocument(String searchString, int page);

    public List<VacancyDTO> getContent(String searchString) {
        return getVacancyList(searchString);
    }

    protected VacancyDTO fillVacancy(String url, String title, String city, String companyName,
                                  String salaryStr, String experience, String format) {
        VacancyDTO vacancy = new VacancyDTO();
        vacancy.setSiteName("hh.ru");
        vacancy.setCity(city);
        vacancy.setTitle(title);
        vacancy.setUrl(url);
        vacancy.setSalary(parseSalary(salaryStr)
                .orElseGet(() -> new SalaryDTO("", 0, "", false)));
        vacancy.setCompanyName(companyName);
        vacancy.setExperience(experience);
        vacancy.setFormat(format);
        return vacancy;
    }

    public Optional<SalaryDTO> parseSalary(String salaryStr) {
        salaryStr = salaryStr.replaceAll("[\\s\u00A0\u202F\u2007,]", "");
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(salaryStr);

        List<Integer> numbers = new ArrayList<>();
        while (m.find()) {
            numbers.add(Integer.parseInt(m.group()));
        }
        if (numbers.isEmpty()) return Optional.empty();

        SalaryDTO salary;
        if (salaryStr.toLowerCase().contains("от") && numbers.size() == 1) {
            if (isRuble(salaryStr)) return Optional.empty();
            salary = new SalaryDTO("от", numbers.getFirst(), RUB_SYMBOL, false);
        } else if (salaryStr.toLowerCase().contains("до") && numbers.size() == 1) {
            if (isRuble(salaryStr)) return Optional.empty();
            salary = new SalaryDTO("до", numbers.getFirst(), RUB_SYMBOL, false);
        } else if (numbers.size() == 2) {
            if (isRuble(salaryStr)) return Optional.empty();
            salary = new SalaryDTO("от " + numbers.get(0) + " до " + numbers.get(1), numbers.get(0), RUB_SYMBOL, true);
        } else if (numbers.size() == 1){
            if (isRuble(salaryStr)) return Optional.empty();
            salary = new SalaryDTO(numbers.getFirst(), RUB_SYMBOL);
        } else {
            return Optional.empty();
        }
        return Optional.of(salary);
    }

    protected boolean isRuble(String string) {
        return string == null || !string.contains(RUB_SYMBOL);
    }
}
