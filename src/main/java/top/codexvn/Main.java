package top.codexvn;

import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.Objects;

public class Main {

    public static void premain(String agentArgs, Instrumentation inst) {
        //parse agentArgs
        System.out.println("agentArgs: " + agentArgs);
        String[] split = agentArgs.split("=");
        Map<String, String> map = Map.of(split[0], split[1]);
        int remoteHostPort = Integer.parseInt(Objects.requireNonNull(map.get("remoteHostPort")));
        inst.addTransformer(new ModifyTransformer(remoteHostPort),true);
    }
}

