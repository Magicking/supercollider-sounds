s.boot;
s.meter;
s.quit;
FreqScope.new
{LFPulse.kr(44056)}.plot;
(
SynthDef.new("pulseTest", {
	arg ampHz=4, fund=40, maxPartial=4, width=0.5;
	var amp1, amp2, sig1, sig2, freq1, freq2;

	amp1 = LFPulse.kr(ampHz,0,0.12) * 0.75;
	amp2 = LFPulse.kr(ampHz,0.5,0.12) * 0.75;

	freq1 = LFNoise0.kr(4).exprange(fund, fund * maxPartial).round(fund);
	freq2 = LFNoise0.kr(4).exprange(fund, fund * maxPartial).round(fund);
	freq1 = freq1 * (LFPulse.kr(8)+1);
	freq2 = freq2 * (LFPulse.kr(6)+1);

	sig1 = Pulse.ar(freq1, width, amp1);
	sig2 = Pulse.ar(freq2, width, amp2);
	sig1 = FreeVerb.ar(sig1, SinOsc.kr(4), 0.8, 0.25);
	sig2 = FreeVerb.ar(sig2, SinOsc.kr(4), 0.8, 0.25);
	Out.ar(0, sig1);
	Out.ar(1, sig2);
}).add;
x = Synth.new("pulseTest");
)


{ [SinOsc.ar(550) * LFPulse.ar(3), SinOsc.ar(440)]}.play;
{ [SinOsc.ar(440) * LFPulse.ar(3), SinOsc.ar(440)]}.play;

(
SynthDef.new("pulseTest", {
	arg ampHz=4, fund=40, maxPartial=4, width=0.5, t_gate=0;
	var amp1, amp2, sig1, sig2, freq1 = 440, freq2 = 440, env;


	env = EnvGen.kr(Env.new(
		[440, 440, 580, 580],
		[1, 1.1, 2.1],
		[1, 1, 1, 1]),
		t_gate);
	sig1 = Pulse.ar(env, width, 0.75);
	sig2 = Pulse.ar(env, width, 0.75);
	Out.ar(0, sig1);
	Out.ar(1, sig1);
}).add;
x = Synth.new("pulseTest");
)
x.set("t_gate", 1);
(
Env.new(
		[440, 440, 580, 580],
		[1, 1.1, 2.1],
		[1, 1, 1, 1]).plot
)
