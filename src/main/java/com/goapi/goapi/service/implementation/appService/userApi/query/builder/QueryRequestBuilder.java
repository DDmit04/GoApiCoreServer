package com.goapi.goapi.service.implementation.appService.userApi.query.builder;

import com.goapi.goapi.domain.model.appService.userApi.request.RequestArgumentType;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;
import com.goapi.goapi.exception.appService.userApi.requestArgument.UserApiRequestArgumentInvalidNameException;
import com.goapi.goapi.exception.appService.userApi.requestArgument.UserApiRequestArgumentMismatchException;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.TemplateArgumentReplaceSupplier;
import com.goapi.goapi.service.implementation.appService.userApi.query.argReplaceSuplier.TemplateArgumentReplaceSupplierFactory;
import com.goapi.goapi.service.implementation.appService.userApi.query.builder.queryElement.ArgQueryRequestStructureElement;
import com.goapi.goapi.service.implementation.appService.userApi.query.builder.queryElement.QueryRequestStructureElement;
import com.goapi.goapi.service.implementation.appService.userApi.query.builder.queryElement.RawQueryRequestStructureElement;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
public class QueryRequestBuilder {

    private List<QueryRequestStructureElement> elementList;
    @Value("${string.request-arg-regex}")
    private String requestArgRegex;
    private final Pattern templateArgPattern = Pattern.compile(requestArgRegex);


    public QueryRequestBuilder(UserApiRequest userApiRequest) {
        elementList = new ArrayList<>();
        String template = userApiRequest.getRequestTemplate();
        int start = 0;
        int end = 0;
        while (start < template.length()) {
            List<MatchResult> results = templateArgPattern
                .matcher(template)
                .results()
                .collect(Collectors.toList());
            for (MatchResult result : results) {
                end = result.start();
                String stringElementValue = template.substring(start, end);
                RawQueryRequestStructureElement newStringElement = new RawQueryRequestStructureElement(stringElementValue);
                start = result.end();

                String argTemplate = template.substring(result.start(), result.end());
                String argName = getArgNameFromArgTemplate(argTemplate);
                RequestArgumentType argumentType = getArgumentTypeByName(userApiRequest, argName);
                QueryRequestStructureElement newArgElement = new ArgQueryRequestStructureElement(argName, argumentType);

                elementList.add(newStringElement);
                elementList.add(newArgElement);
            }
        }
    }

    private String getArgNameFromArgTemplate(String argTemplate) {
        Matcher matcher = templateArgPattern.matcher(argTemplate);
        if (matcher.find())
        {
            String argName = matcher.group(1);
            return argName;
        }
        throw new UserApiRequestArgumentInvalidNameException();
    }

    public void setArgument(String argName, Object argValue) {
        elementList.stream()
            .filter(element -> element.getName().equals(argName))
            .findFirst()
            .map(arg -> {
                RequestArgumentType argumentType = arg.getArgumentType();
                TemplateArgumentReplaceSupplier argumentReplaceSupplier = TemplateArgumentReplaceSupplierFactory.getArgumentReplaceSupplier(argumentType);
                String argStringValue = argumentReplaceSupplier.getTemplateArgumentReplacement(argValue);
                arg.setValue(argStringValue);
                return arg;
            })
            .orElseThrow(() -> new UserApiRequestArgumentMismatchException(argName));
    }

    public String getQuery() {
        StringBuilder res = new StringBuilder();
        elementList.forEach(elem -> res.append(elem.getValue()));
        res.append(";");
        return res.toString();
    }

    private RequestArgumentType getArgumentTypeByName(UserApiRequest userApiRequest, String argName) {
        Optional<UserApiRequestArgument> argumentOptional = userApiRequest
            .getUserApiRequestArguments()
            .stream()
            .filter(ar -> ar.getArgName().equals(argName))
            .findFirst();
        return argumentOptional.map(arg -> {
            RequestArgumentType argumentType = arg.getRequestArgumentType();
            return argumentType;
        }).orElseThrow(() -> new UserApiRequestArgumentMismatchException(argName));
    }
}
