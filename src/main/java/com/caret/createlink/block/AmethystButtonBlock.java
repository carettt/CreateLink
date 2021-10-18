package com.caret.createlink.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.world.World;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class AmethystButtonBlock extends AbstractButtonBlock {
    public AmethystButtonBlock(AbstractBlock.Properties properties) {
        super(false, properties);
    }

    private boolean powered = false;

    protected SoundEvent getSoundEvent(boolean isOn) {
        return isOn ? SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON : SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF;
    }

    private String sendHttpPostRequest(String targetUrl) {
        try {
            System.out.println("SENDING POST REQUEST!");
            //set target url
            URL url = new URL(targetUrl);

            //build request body
            JSONObject jsonData = new JSONObject();
            jsonData.put("powered", this.powered);

            //setup connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            //send request
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(jsonData.toJSONString());
            wr.close();
            System.out.println("REQUEST SENT");

            //parse response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append("\r");
            }
            rd.close();

            String response = responseBuilder.toString();

            Object responseObject = new JSONParser().parse(response);

            JSONObject responseJSON = (JSONObject) responseObject;

            return (String) responseJSON.get("status");
        } catch (Exception ex) {
            System.out.println(ex);
            return "UNSUCCESSFUL";
        }

    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (state.get(POWERED)) {
            return ActionResultType.CONSUME;
        } else {
            this.powerBlock(state, worldIn, pos);
            this.playSound(player, worldIn, pos, true);
            if (!worldIn.isRemote) {
                //send http request to server
                //replace 127.0.0.1:3000 to actual server url
                powered = !powered;
                String httpStatus = sendHttpPostRequest("http://127.0.0.1:3000/api");
                //check status of request
                if (httpStatus.equals("UNSUCCESSFUL")) {
                    System.out.println("HTTP POST Request was unsuccessful!");
                }
            }
            return ActionResultType.func_233537_a_(worldIn.isRemote);
        }
    }
}